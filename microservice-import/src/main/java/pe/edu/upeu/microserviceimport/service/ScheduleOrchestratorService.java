package pe.edu.upeu.microserviceimport.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.microserviceimport.client.*;
import pe.edu.upeu.microserviceimport.dto.*;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.text.Normalizer;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class ScheduleOrchestratorService {

    @Autowired
    private ExcelReaderService excelReaderService;

    @Autowired
    private CourseManagementClient courseManagementClient;

    @Autowired
    private GroupClient groupClient;

    @Autowired
    private CycleClient cycleClient;

    @Autowired
    private FacultyClient facultyClient;

    @Autowired
    private ProfessionalSchoolClient professionalSchoolClient;

    @Autowired
    private PlanClient planClient;

    @Autowired
    private ScheduleClient scheduleClient;

    @Autowired
    private EnvironmentClient environmentClient;

    @Autowired
    private TeacherClient teacherClient;

    @Autowired
    private CourseAssignmentCourseClient courseAssignmentCourseClient;

    @Autowired
    private CourseTypeClient courseTypeClient;

    @Autowired
    private WeekDayClient weekDayClient;

    // Mapa temporal de capacidad por ciclo calculado desde el Excel
    private Map<Integer, Integer> cycleCapacityMap = new HashMap<>();

    // Horarios predefinidos de lunes a viernes
    private static final LocalTime[] START_TIMES = {
            LocalTime.of(8, 0),   // 8:00 AM
            LocalTime.of(9, 30),  // 9:30 AM
            LocalTime.of(11, 0),  // 11:00 AM
            LocalTime.of(12, 30), // 12:30 PM
            LocalTime.of(14, 0),  // 2:00 PM
            LocalTime.of(15, 30), // 3:30 PM
            LocalTime.of(17, 0)   // 5:00 PM
    };

    private static final String[] DAYS_OF_WEEK = {
            "LUNES", "MARTES", "MIÉRCOLES", "JUEVES", "VIERNES"
    };

    private static final int SLOT_DURATION_MINUTES = 90; // 1.5 horas

    public void processAndCreateSchedules(java.io.InputStream excelInputStream) throws Exception {
        log.info("========== INICIANDO PROCESO DE ORQUESTACIÓN ==========");

        try {
            log.info("Limpiando todos los horarios existentes de la base de datos para una importación limpia...");
            scheduleClient.deleteAllSchedules();
            log.info("✓ Horarios antiguos eliminados con éxito");
        } catch (Exception e) {
            log.warn("No se pudieron eliminar los horarios existentes: {}", e.getMessage());
        }

        // Paso 1: Leer Excel
        log.info("Paso 1: Leyendo archivo Excel...");
        List<CargaPsicoExcelDTO> cursos = excelReaderService.readExcel(excelInputStream);
        log.info("✓ {} cursos leídos del Excel", cursos.size());

        // Calcular capacidad por ciclo (máximo aforo observado en el ciclo)
        this.cycleCapacityMap = computeCycleCapacityMap(cursos);

        // Paso 2: Obtener espacios académicos disponibles
        log.info("Paso 2: Obteniendo espacios académicos...");
        List<AcademicSpaceDTO> espacios = environmentClient.getAllAcademicSpaces();
        log.info("✓ {} espacios académicos disponibles", espacios.size());

        if (espacios.isEmpty()) {
            throw new Exception("No hay espacios académicos disponibles en el sistema");
        }

        Map<Long, String> weekDayIds = loadWeekDayIds();
        Set<String> occupiedSlots = loadExistingOccupiedSlots(weekDayIds);
        Map<String, TeacherDTO> teachersByKey = loadExistingTeachersByKey();
        Map<String, CourseResponseDTO> existingCoursesByCode = loadExistingCoursesByCode();

        // Paso 3: Agrupar cursos por ciclo y grupo
        log.info("Paso 3: Agrupando cursos por ciclo y grupo...");
        Map<String, List<CargaPsicoExcelDTO>> cursosAgrupados = agruparCursosPorCicloGrupo(cursos);
        log.info("✓ {} grupos diferentes identificados", cursosAgrupados.size());

        // Paso 4: Crear cursos y asignar horarios
        log.info("Paso 4: Creando cursos y asignando horarios...");
        int cursosCreados = 0;
        int horariosAsignados = 0;

        ScheduleTracker scheduleTracker = new ScheduleTracker(occupiedSlots);

        for (Map.Entry<String, List<CargaPsicoExcelDTO>> entry : cursosAgrupados.entrySet()) {
            String grupoKey = entry.getKey();
            List<CargaPsicoExcelDTO> cursosDelGrupo = entry.getValue();

            log.info("Procesando grupo: {}", grupoKey);

            // Collect courses to auto-assign after creating them
            List<pe.edu.upeu.microserviceimport.dto.request.CourseToAssignRequest> toAssign = new ArrayList<>();

            for (CargaPsicoExcelDTO curso : cursosDelGrupo) {
                try {
                    // Crear curso
                    CreateCourseDTO courseDTO = convertirACreateCourseDTO(curso);
                    String generatedCode = normalizeText(courseDTO.getCode());

                    CourseResponseDTO cursoCreado;
                    if (existingCoursesByCode.containsKey(generatedCode)) {
                        log.info("✓ Curso existente recuperado: {}", curso.getNombreCurso());
                        cursoCreado = existingCoursesByCode.get(generatedCode);
                    } else {
                        cursoCreado = courseManagementClient.createCourse(courseDTO);
                        cursosCreados++;
                        log.info("✓ Curso creado: {} (ID: {})", curso.getNombreCurso(), cursoCreado.getIdCourse());
                        existingCoursesByCode.put(generatedCode, cursoCreado);
                    }

                    // Obtener o crear asignación de docente
                    Long idCourseAssignment = obtenerOCrearCourseAssignment(curso, cursoCreado.getIdCourse(), teachersByKey);

                    if (idCourseAssignment == null) {
                        log.warn("No se asignó docente para el curso {}, se omite el horario", curso.getNombreCurso());
                        continue;
                    }

                    // Prepare candidate academic spaces for this course
                    List<AcademicSpaceDTO> espaciosPreferidos = filtrarEspaciosPorPreferencia(espacios, curso.getAmbienteEspecializado());
                    List<Long> candidateIds = espaciosPreferidos.stream()
                            .filter(e -> e.getCapacity() >= (curso.getAforoPorCursoGrupo() != null ? curso.getAforoPorCursoGrupo() : 30))
                            .map(AcademicSpaceDTO::getIdAcademicSpace)
                            .collect(Collectors.toList());

                    if (candidateIds.isEmpty()) {
                        log.warn("No hay espacios candidatos para curso {}", curso.getNombreCurso());
                    } else {
                        Long resolvedTypeHourId = 1L; // default HT (Teórica)
                        if (curso.getHp() != null && curso.getHp() > 0 && (curso.getHt() == null || curso.getHt() == 0)) {
                            resolvedTypeHourId = 2L; // HP (Práctica)
                        }
                        Integer hoursRequired = (resolvedTypeHourId == 1L) ? curso.getHt() : curso.getHp();
                        if (hoursRequired == null || hoursRequired <= 0) {
                            hoursRequired = 2;
                        }
                        pe.edu.upeu.microserviceimport.dto.request.CourseToAssignRequest cta = pe.edu.upeu.microserviceimport.dto.request.CourseToAssignRequest.builder()
                                .idCourseAssignment(idCourseAssignment)
                                .capacityRequired(curso.getAforoPorCursoGrupo() != null ? curso.getAforoPorCursoGrupo() : 30)
                                .preferredType(curso.getAmbienteEspecializado())
                                .candidateAcademicSpaceIds(candidateIds)
                                .idTypeSchedule(resolvedTypeHourId)
                                .hoursRequired(hoursRequired)
                                .build();
                        toAssign.add(cta);
                    }

                } catch (Exception e) {
                    log.error("Error al procesar curso {}: {}", curso.getNombreCurso(), e.getMessage());
                }
            }

            // After creating all courses in the group, call schedule auto-assign
            if (!toAssign.isEmpty()) {
                pe.edu.upeu.microserviceimport.dto.request.AutoAssignRequest req = pe.edu.upeu.microserviceimport.dto.request.AutoAssignRequest.builder()
                        .courses(toAssign)
                        .startTimes(Arrays.stream(START_TIMES).map(LocalTime::toString).collect(Collectors.toList()))
                        .durationMinutes(SLOT_DURATION_MINUTES)
                        .weekDayIds(Arrays.stream(DAYS_OF_WEEK).map(d -> resolveWeekDayId(weekDayIds, d)).filter(Objects::nonNull).collect(Collectors.toList()))
                        .build();

                try {
                    var assignResp = scheduleClient.autoAssign(req);
                    if (assignResp != null && assignResp.getAssigned() != null) {
                        horariosAsignados += assignResp.getAssigned().size();
                        // mark occupied slots
                        for (var a : assignResp.getAssigned()) {
                            java.time.LocalTime s = java.time.LocalTime.parse(a.getStartTime());
                            java.time.LocalTime e = java.time.LocalTime.parse(a.getEndTime());
                            scheduleTracker.markSlotAsOccupied(buildSlotKey(a.getWeekDayId(), s, e, a.getIdAcademicSpace()));
                        }
                    }
                    if (assignResp != null && assignResp.getFailedCourseAssignmentIds() != null && !assignResp.getFailedCourseAssignmentIds().isEmpty()) {
                        log.warn("Cursos sin asignar en grupo {}: {}", grupoKey, assignResp.getFailedCourseAssignmentIds());
                    }
                } catch (Exception ex) {
                    log.error("Error al llamar auto-assign: {}", ex.getMessage());
                }
            }

        }

        log.info("========== PROCESO COMPLETADO ==========");
        log.info("✓ Cursos creados: {}", cursosCreados);
        log.info("✓ Horarios asignados: {}", horariosAsignados);
        log.info("========== FIN DEL PROCESO ==========");
    }

    private Map<String, List<CargaPsicoExcelDTO>> agruparCursosPorCicloGrupo(List<CargaPsicoExcelDTO> cursos) {
        Map<String, List<CargaPsicoExcelDTO>> agrupados = new HashMap<>();

        for (CargaPsicoExcelDTO curso : cursos) {
            Integer grupoVal = curso.getGrupo();
            String grupoPart = (grupoVal != null) ? String.valueOf(grupoVal) : "UNICO";
            String key = String.format("CICLO_%d_GRUPO_%s", curso.getCiclo(), grupoPart);
            agrupados.computeIfAbsent(key, k -> new ArrayList<>()).add(curso);
        }

        return agrupados;
    }

    private Map<Integer, Integer> computeCycleCapacityMap(List<CargaPsicoExcelDTO> cursos) {
        Map<Integer, Integer> map = new HashMap<>();
        for (CargaPsicoExcelDTO c : cursos) {
            Integer ciclo = c.getCiclo() != null ? c.getCiclo() : 1;
            Integer cap = c.getAforoPorCursoGrupo();
            if (cap == null || cap <= 0) continue;
            map.put(ciclo, Math.max(map.getOrDefault(ciclo, 0), cap));
        }
        return map;
    }

    private CreateCourseDTO convertirACreateCourseDTO(CargaPsicoExcelDTO cursoExcel) {
        Long resolvedGroupId = 1L;
        try {
            resolvedGroupId = getOrCreateGroupId(cursoExcel);
        } catch (Exception e) {
            log.warn("No se pudo resolver/crear grupo para curso {}: {}. Usando idGroup=1", cursoExcel.getNombreCurso(), e.getMessage());
        }

        return CreateCourseDTO.builder()
                .name(cursoExcel.getNombreCurso())
                .code(generarCodigoCurso(cursoExcel))
            .description(String.format("Ciclo: %d, Grupo: %s, Modalidad: %s, Docente: %s",
                cursoExcel.getCiclo(), cursoExcel.getGrupo() != null ? String.valueOf(cursoExcel.getGrupo()) : "UNICO", cursoExcel.getModalidad(), cursoExcel.getDocente()))
            .duration(cursoExcel.getTotalHoras() != null ? cursoExcel.getTotalHoras() * 60 : 0)
            .theoreticalHours(cursoExcel.getHt() != null ? cursoExcel.getHt() * 60 : 0)
            .practicalHours(cursoExcel.getHp() != null ? cursoExcel.getHp() * 60 : 0)
            .totalHours(java.time.Duration.ofHours(cursoExcel.getTotalHoras() != null ? cursoExcel.getTotalHoras() : 0))
                .idCourseType(getOrCreateCourseTypeId(cursoExcel))
                .idPlan(getOrCreatePlanId(cursoExcel.getPlan())) // Resuelve o crea plan
                .idGroup(resolvedGroupId)
                .build();
    }

    private Long getOrCreateGroupId(CargaPsicoExcelDTO cursoExcel) {
        // Primero resolver o crear el ciclo correspondiente; asegurar que exista la escuela profesional
        Long idProfessionalSchool = getOrCreateProfessionalSchoolId(cursoExcel.getEscuela(), cursoExcel.getFacultad());
        Long idCycle = getOrCreateCycleId(cursoExcel.getCiclo(), idProfessionalSchool);

        // Buscar en MS-COURSE-MANAGEMENT grupos existentes
        List<pe.edu.upeu.microserviceimport.dto.external.GroupResponseDTO> groups = groupClient.findAll();
        String targetGroupNumber = cursoExcel.getGrupo() != null ? String.valueOf(cursoExcel.getGrupo()) : "UNICO";
        for (pe.edu.upeu.microserviceimport.dto.external.GroupResponseDTO g : groups) {
            if (g.getGroupNumber() != null && g.getGroupNumber().trim().equalsIgnoreCase(targetGroupNumber)) {
                if (g.getCycle() != null && Objects.equals(g.getCycle().getIdCycle(), idCycle)) {
                    return g.getIdGroup();
                }
            }
        }

        // Si no existe, crear
        // Usar aforo del curso si existe, si no usar capacidad por ciclo si está disponible
        int capacity = 30;
        if (cursoExcel.getAforoPorCursoGrupo() != null && cursoExcel.getAforoPorCursoGrupo() > 0) {
            capacity = cursoExcel.getAforoPorCursoGrupo();
        } else {
            Integer cap = this.cycleCapacityMap.get(cursoExcel.getCiclo());
            if (cap != null && cap > 0) capacity = cap;
        }
        pe.edu.upeu.microserviceimport.dto.external.GroupCreateRequestDTO req = pe.edu.upeu.microserviceimport.dto.external.GroupCreateRequestDTO.builder()
                .groupNumber(targetGroupNumber)
                .capacity(capacity)
                .idCycle(idCycle)
                .build();

        var created = groupClient.createGroup(req);
        return created != null && created.getIdGroup() != null ? created.getIdGroup() : 1L;
    }

    private Long getOrCreateCycleId(Integer cicloNumber) {
        String name = "CICLO " + (cicloNumber != null ? cicloNumber : 1);
        List<pe.edu.upeu.microserviceimport.dto.external.CycleResponseDTO> cycles = cycleClient.findAll();
        for (pe.edu.upeu.microserviceimport.dto.external.CycleResponseDTO c : cycles) {
            if (c.getName() != null && c.getName().trim().equalsIgnoreCase(name)) {
                return c.getIdCycle();
            }
        }

        // Crear ciclo usando la escuela profesional (si está disponible)
        Long idProfessionalSchool = 1L; // default
        try {
            // Intentar inferir la escuela/profesional a partir del contexto; si el DTO contiene escuela, usarla
            // Si no hay contexto a este punto, se usará el idProfessionalSchool por defecto
        } catch (Exception ignored) {}

        pe.edu.upeu.microserviceimport.dto.external.CycleCreateRequestDTO creq = pe.edu.upeu.microserviceimport.dto.external.CycleCreateRequestDTO.builder()
                .name(name)
                .idProfessionalSchool(idProfessionalSchool)
                .build();

        var created = cycleClient.createCycle(creq);
        return created != null && created.getIdCycle() != null ? created.getIdCycle() : 1L;
    }

    private Long getOrCreateCycleId(Integer cicloNumber, Long idProfessionalSchool) {
        String name = "CICLO " + (cicloNumber != null ? cicloNumber : 1);
        List<pe.edu.upeu.microserviceimport.dto.external.CycleResponseDTO> cycles = cycleClient.findAll();
        for (pe.edu.upeu.microserviceimport.dto.external.CycleResponseDTO c : cycles) {
            if (c.getName() != null && c.getName().trim().equalsIgnoreCase(name)) {
                return c.getIdCycle();
            }
        }

        pe.edu.upeu.microserviceimport.dto.external.CycleCreateRequestDTO creq = pe.edu.upeu.microserviceimport.dto.external.CycleCreateRequestDTO.builder()
                .name(name)
                .idProfessionalSchool(idProfessionalSchool != null ? idProfessionalSchool : 1L)
                .build();

        var created = cycleClient.createCycle(creq);
        return created != null && created.getIdCycle() != null ? created.getIdCycle() : 1L;
    }

    private Long getOrCreateFacultyId(String facultyName) {
        if (facultyName == null || facultyName.isBlank()) return 1L;
        try {
            List<pe.edu.upeu.microserviceimport.dto.external.FacultyResponseDTO> list = facultyClient.findAll();
            for (var f : list) {
                if (f.getName() != null && f.getName().trim().equalsIgnoreCase(facultyName.trim())) {
                    return f.getIdFaculty();
                }
            }
            var created = facultyClient.create(pe.edu.upeu.microserviceimport.dto.external.FacultyCreateRequestDTO.builder().name(facultyName.trim()).build());
            if (created != null && created.getIdFaculty() != null) return created.getIdFaculty();
        } catch (Exception e) {
            log.warn("No se pudo resolver/crear Faculty '{}': {}", facultyName, e.getMessage());
        }
        return 1L;
    }

    private Long getOrCreateProfessionalSchoolId(String schoolName, String facultyName) {
        if (schoolName == null || schoolName.isBlank()) return 1L;
        try {
            List<pe.edu.upeu.microserviceimport.dto.external.ProfessionalSchoolResponseDTO> list = professionalSchoolClient.findAll();
            for (var s : list) {
                if (s.getName() != null && s.getName().trim().equalsIgnoreCase(schoolName.trim())) {
                    return s.getIdProfessionalSchool();
                }
            }
            Long idFaculty = getOrCreateFacultyId(facultyName);
            var created = professionalSchoolClient.create(pe.edu.upeu.microserviceimport.dto.external.ProfessionalSchoolCreateRequestDTO.builder().name(schoolName.trim()).idFaculty(idFaculty).build());
            if (created != null && created.getIdProfessionalSchool() != null) return created.getIdProfessionalSchool();
        } catch (Exception e) {
            log.warn("No se pudo resolver/crear ProfessionalSchool '{}': {}", schoolName, e.getMessage());
        }
        return 1L;
    }

    private Long getOrCreatePlanId(String planName) {
        if (planName == null || planName.isBlank()) return 1L;
        try {
            List<pe.edu.upeu.microserviceimport.dto.external.PlanResponseDTO> list = planClient.findAll();
            for (var p : list) {
                if (p.getName() != null && p.getName().trim().equalsIgnoreCase(planName.trim())) {
                    return p.getIdPlan();
                }
            }
            var created = planClient.create(pe.edu.upeu.microserviceimport.dto.external.PlanCreateRequestDTO.builder().name(planName.trim()).build());
            if (created != null && created.getIdPlan() != null) return created.getIdPlan();
        } catch (Exception e) {
            log.warn("No se pudo resolver/crear Plan '{}': {}", planName, e.getMessage());
        }
        return 1L;
    }

    private Long getOrCreateCourseTypeId(CargaPsicoExcelDTO cursoExcel) {
        String typeName = cursoExcel.getModalidad();
        if (typeName == null || typeName.isBlank()) {
            typeName = cursoExcel.getModo();
        }

        if (typeName == null || typeName.isBlank() || typeName.trim().equalsIgnoreCase("Regular")) {
            typeName = "PRESENCIAL";
        } else {
            String upper = typeName.trim().toUpperCase();
            if (upper.contains("SÍNCRÓNICO") || upper.contains("SINCRONICO") || 
                upper.contains("SÍCRÓNICO") || upper.contains("SICRONICO") ||
                upper.contains("SINCRO") || upper.contains("SICRO") ||
                upper.contains("VIRTUAL") || upper.contains("VIRT")) {
                typeName = "SÍNCRÓNICO";
            } else if (upper.contains("SEMIPRESENCIAL")) {
                typeName = "SEMIPRESENCIAL";
            } else if (upper.contains("PRESENCIAL")) {
                typeName = "PRESENCIAL";
            } else {
                typeName = "PRESENCIAL";
            }
        }

        try {
            List<pe.edu.upeu.microserviceimport.dto.external.CourseTypeResponseDTO> list = courseTypeClient.findAll();
            for (var t : list) {
                if (t.getName() != null && t.getName().trim().equalsIgnoreCase(typeName.trim())) {
                    return t.getIdCourseType();
                }
            }
            var created = courseTypeClient.create(pe.edu.upeu.microserviceimport.dto.external.CourseTypeCreateRequestDTO.builder().name(typeName.trim()).build());
            if (created != null && created.getIdCourseType() != null) return created.getIdCourseType();
        } catch (Exception e) {
            log.warn("No se pudo resolver/crear CourseType '{}': {}", typeName, e.getMessage());
        }
        return 1L;
    }


    private String generarCodigoCurso(CargaPsicoExcelDTO curso) {
        String slug = slugify(curso.getNombreCurso());
        String digest = shortDigest(buildCourseKey(curso));
        String grupoPart = curso.getGrupo() != null ? String.valueOf(curso.getGrupo()) : "UNICO";
        return String.format("PSI-%d-%s-%s-%s",
            curso.getCiclo(),
            grupoPart,
            slug,
            digest);
    }

    private Long obtenerOCrearCourseAssignment(CargaPsicoExcelDTO curso, Long idCourse, Map<String, TeacherDTO> teachersByKey) {
        if (curso.getDocente() == null || curso.getDocente().isBlank()) {
            log.warn("Curso {} sin docente asignado", curso.getNombreCurso());
            return null;
        }

        TeacherDTO teacher = findTeacherByAcademicName(teachersByKey, curso.getDocente());

        if (teacher == null) {
            String docName = curso.getDocente().trim();
            String[] parts = docName.split("\\s+");
            String tName = docName;
            String tLastName = ".";
            
            if (parts.length == 2) {
                tLastName = parts[0];
                tName = parts[1];
            } else if (parts.length == 3) {
                tLastName = parts[0] + " " + parts[1];
                tName = parts[2];
            } else if (parts.length >= 4) {
                tLastName = parts[0] + " " + parts[1];
                tName = String.join(" ", Arrays.copyOfRange(parts, 2, parts.length));
            }

            TeacherDTO newTeacher = TeacherDTO.builder()
                    .name(tName)
                    .lastName(tLastName)
                    .email(docName.replaceAll("\\s+", "").toLowerCase() + "@upeu.edu.pe")
                    .build();
            teacher = teacherClient.createTeacher(newTeacher);
            teachersByKey.put(buildTeacherKeyFromAcademicName(curso.getDocente()), teacher);
        }

        CourseAssignmentDTO assignmentDTO = CourseAssignmentDTO.builder()
                .idTeacher(teacher.getIdTeacher())
                .build();

        CourseAssignmentDTO assignment = courseManagementClient.createCourseAssignment(assignmentDTO);

        if (idCourse != null && assignment.getIdCourseAssignment() != null) {
            CourseAssignmentCourseDTO link = CourseAssignmentCourseDTO.builder()
                    .idCourse(idCourse)
                    .idCourseAssignment(assignment.getIdCourseAssignment())
                    .build();
            courseAssignmentCourseClient.create(link);
        }

        return assignment.getIdCourseAssignment();
    }

    private boolean asignarHorarioSinConflictos(
            CourseResponseDTO curso,
            Long idCourseAssignment,
            List<AcademicSpaceDTO> espacios,
            ScheduleTracker scheduleTracker,
            CargaPsicoExcelDTO cursoExcel,
            Map<Long, String> weekDayIds) {

        // Necesitamos 3 horarios por semana (lunes, miércoles, viernes)
        int horariosNecesarios = 3;
        int horariosAsignados = 0;
        List<AcademicSpaceDTO> espaciosPreferidos = filtrarEspaciosPorPreferencia(espacios, cursoExcel.getAmbienteEspecializado());

        for (String dia : DAYS_OF_WEEK) {
            if (horariosAsignados >= horariosNecesarios) {
                break;
            }

            Long weekDayId = resolveWeekDayId(weekDayIds, dia);
            if (weekDayId == null) {
                log.warn("No se encontró WeekDay para {}", dia);
                continue;
            }

            for (AcademicSpaceDTO espacio : espaciosPreferidos) {
                // Verificar capacidad del espacio
                if (espacio.getCapacity() < (cursoExcel.getAforoPorCursoGrupo() != null ? cursoExcel.getAforoPorCursoGrupo() : 30)) {
                    continue;
                }

                // Buscar un horario disponible
                for (LocalTime startTime : START_TIMES) {
                    LocalTime endTime = startTime.plusMinutes(SLOT_DURATION_MINUTES);

                    String slotKey = buildSlotKey(weekDayId, startTime, endTime, espacio.getIdAcademicSpace());

                    // Verificar si el slot está disponible
                    if (!scheduleTracker.isSlotOccupied(slotKey)) {
                        try {
                            // Crear el horario
                            CreateScheduleDTO scheduleDTO = CreateScheduleDTO.builder()
                                    .startTime(startTime)
                                    .endTime(endTime)
                                    .duration(SLOT_DURATION_MINUTES)
                                    .dayName(dia)
                                    .idAcademicSpace(espacio.getIdAcademicSpace())
                                    .idCourseAssignment(idCourseAssignment)
                                    .idTypeSchedule(1L) // Predeterminado
                                        .idWeekName(weekDayId)
                                    .build();

                                    scheduleClient.createSchedule(scheduleDTO);
                            scheduleTracker.markSlotAsOccupied(slotKey);
                            horariosAsignados++;

                            log.info("✓ Horario asignado: {} {} - {} en sala {}", 
                                    dia, startTime, endTime, espacio.getSpaceName());
                            break;

                        } catch (Exception e) {
                            log.error("Error al crear horario: {}", e.getMessage());
                        }
                    }
                }

                if (horariosAsignados >= horariosNecesarios) {
                    break;
                }
            }
        }

        return horariosAsignados == horariosNecesarios;
    }

    /**
     * Tracker para mantener registro de horarios ocupados
     * Previene conflictos en mismo salón + mismo horario
     */
    private static class ScheduleTracker {
        private final Set<String> occupiedSlots;

        private ScheduleTracker(Set<String> occupiedSlots) {
            this.occupiedSlots = occupiedSlots;
        }

        public boolean isSlotOccupied(String slotKey) {
            return occupiedSlots.contains(slotKey);
        }

        public void markSlotAsOccupied(String slotKey) {
            occupiedSlots.add(slotKey);
        }
    }

    private Map<String, TeacherDTO> loadExistingTeachersByKey() {
        try {
            List<TeacherDTO> teachers = teacherClient.getAllTeachers();
            if (teachers == null) {
                return new HashMap<>();
            }

            Map<String, TeacherDTO> teachersByKey = new HashMap<>();
            for (TeacherDTO teacher : teachers) {
                teachersByKey.put(buildTeacherKey(teacher.getName(), teacher.getLastName()), teacher);
                teachersByKey.put(buildTeacherKey(teacher.getLastName(), teacher.getName()), teacher);
                teachersByKey.put(normalizeText(teacher.getName()), teacher);
                teachersByKey.put(normalizeText(teacher.getLastName()), teacher);
            }

            return teachersByKey;
        } catch (Exception e) {
            log.warn("No se pudieron cargar docentes existentes: {}", e.getMessage());
            return new HashMap<>();
        }
    }

    private Map<String, CourseResponseDTO> loadExistingCoursesByCode() {
        try {
            List<CourseResponseDTO> courses = courseManagementClient.getAllCourses();
            if (courses == null) {
                return new HashMap<>();
            }

            return courses.stream()
                    .filter(course -> course.getCode() != null)
                    .collect(Collectors.toMap(
                            course -> normalizeText(course.getCode()),
                            course -> course,
                            (existing, replacement) -> existing,
                            HashMap::new));
        } catch (Exception e) {
            log.warn("No se pudieron cargar cursos existentes: {}", e.getMessage());
            return new HashMap<>();
        }
    }

    private Map<Long, String> loadWeekDayIds() {
        try {
            List<WeekDayDTO> weekDays = weekDayClient.getAllWeekDays();
            Map<Long, String> weekDayIds = new HashMap<>();
            if (weekDays != null && !weekDays.isEmpty()) {
                for (WeekDayDTO weekDay : weekDays) {
                    if (weekDay.getIdSchedule() != null && weekDay.getName() != null) {
                        weekDayIds.put(weekDay.getIdSchedule(), weekDay.getName().toUpperCase());
                    }
                }
            } else {
                weekDayIds.put(1L, "DOMINGO");
                weekDayIds.put(2L, "LUNES");
                weekDayIds.put(3L, "MARTES");
                weekDayIds.put(4L, "MIÉRCOLES");
                weekDayIds.put(5L, "JUEVES");
                weekDayIds.put(6L, "VIERNES");
                weekDayIds.put(7L, "SÁBADO");
            }
            return weekDayIds;
        } catch (Exception e) {
            log.warn("No se pudieron cargar los días de semana: {}", e.getMessage());
            Map<Long, String> fallback = new HashMap<>();
            fallback.put(1L, "DOMINGO");
            fallback.put(2L, "LUNES");
            fallback.put(3L, "MARTES");
            fallback.put(4L, "MIÉRCOLES");
            fallback.put(5L, "JUEVES");
            fallback.put(6L, "VIERNES");
            fallback.put(7L, "SÁBADO");
            return fallback;
        }
    }

    private Set<String> loadExistingOccupiedSlots(Map<Long, String> weekDayIds) {
        Set<String> occupiedSlots = new HashSet<>();
        try {
            List<CreateScheduleDTO> schedules = scheduleClient.getAllSchedules();
            if (schedules != null) {
                for (CreateScheduleDTO schedule : schedules) {
                    if (schedule.getIdWeekName() != null && schedule.getIdAcademicSpace() != null && schedule.getStartTime() != null && schedule.getEndTime() != null) {
                        occupiedSlots.add(buildSlotKey(schedule.getIdWeekName(), schedule.getStartTime(), schedule.getEndTime(), schedule.getIdAcademicSpace()));
                    }
                }
            }
        } catch (Exception e) {
            log.warn("No se pudieron cargar horarios existentes: {}", e.getMessage());
        }
        return occupiedSlots;
    }

    private List<AcademicSpaceDTO> filtrarEspaciosPorPreferencia(List<AcademicSpaceDTO> espacios, String preferencia) {
        if (preferencia == null || preferencia.isBlank()) {
            return espacios;
        }

        String normalized = preferencia.trim().toLowerCase();
        List<AcademicSpaceDTO> filtrados = espacios.stream()
                .filter(espacio -> matchesPreference(espacio, normalized))
                .toList();

        return filtrados.isEmpty() ? espacios : filtrados;
    }

    public boolean matchesPreference(AcademicSpaceDTO espacio, String preference) {
        if (preference == null || preference.isBlank()) {
            return true;
        }
        String normalizedPref = preference.trim().toLowerCase();

        // Bidirectional contains comparison for type name
        boolean typeMatch = false;
        if (espacio.getTypeAcademicSpace() != null && espacio.getTypeAcademicSpace().getName() != null) {
            String typeName = espacio.getTypeAcademicSpace().getName().toLowerCase();
            typeMatch = typeName.contains(normalizedPref) || normalizedPref.contains(typeName);
            // Also handle common abbreviations like "lab" matching "laboratorio"
            if (normalizedPref.contains("lab") && typeName.contains("laboratorio")) {
                typeMatch = true;
            }
        }

        // Bidirectional contains comparison for space name
        boolean nameMatch = containsIgnoreCase(espacio.getSpaceName(), normalizedPref)
                || (espacio.getSpaceName() != null && normalizedPref.contains(espacio.getSpaceName().toLowerCase()));

        // Handle common abbreviations like "lab" matching "laboratorio" in names
        if (normalizedPref.contains("lab") && contieneLab(espacio.getSpaceName())) {
            nameMatch = true;
        }

        return typeMatch
                || nameMatch
                || containsIgnoreCase(espacio.getObservation(), normalizedPref)
                || containsIgnoreCase(espacio.getLocation(), normalizedPref);
    }

    private boolean contieneLab(String spaceName) {
        if (spaceName == null) return false;
        String s = spaceName.toLowerCase();
        return s.contains("lab") || s.contains("laboratorio");
    }

    private boolean containsIgnoreCase(String value, String search) {
        return value != null && value.toLowerCase().contains(search);
    }

    private Long resolveWeekDayId(Map<Long, String> weekDayIds, String dayName) {
        String normalizedDay = normalizeText(dayName);
        for (Map.Entry<Long, String> entry : weekDayIds.entrySet()) {
            if (entry.getValue() != null && normalizeText(entry.getValue()).equals(normalizedDay)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private String normalizeText(String value) {
        if (value == null) {
            return "";
        }

        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "").trim().toUpperCase();
    }

    private String buildTeacherKeyFromAcademicName(String academicTeacherName) {
        return normalizeText(academicTeacherName);
    }

    private TeacherDTO findTeacherByAcademicName(Map<String, TeacherDTO> teachersByKey, String academicTeacherName) {
        for (String candidateKey : buildTeacherLookupKeys(academicTeacherName)) {
            TeacherDTO teacher = teachersByKey.get(candidateKey);
            if (teacher != null) {
                return teacher;
            }
        }

        return null;
    }

    private List<String> buildTeacherLookupKeys(String academicTeacherName) {
        String normalized = normalizeText(academicTeacherName);
        if (normalized.isBlank()) {
            return List.of();
        }

        String[] parts = normalized.split("\\s+");
        Set<String> keys = new LinkedHashSet<>();
        keys.add(normalized);

        if (parts.length == 1) {
            return new ArrayList<>(keys);
        }

        String firstPart = parts[0];
        String remainder = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
        keys.add(buildTeacherKey(firstPart, remainder));
        keys.add(buildTeacherKey(remainder, firstPart));

        return new ArrayList<>(keys);
    }

    private String buildTeacherKey(String name, String lastName) {
        return normalizeText((name == null ? "" : name) + " " + (lastName == null ? "" : lastName));
    }

    private String buildCourseKey(CargaPsicoExcelDTO curso) {
        return String.join("|",
                normalizeText(curso.getNombreCurso()),
                String.valueOf(curso.getCiclo()),
                curso.getGrupo() != null ? String.valueOf(curso.getGrupo()) : "UNICO",
                normalizeText(curso.getPlan()));
    }

    private String slugify(String value) {
        String normalized = normalizeText(value);
        return normalized.replaceAll("[^A-Z0-9]+", "-")
                .replaceAll("^-+|-+$", "")
                .substring(0, Math.min(18, normalized.replaceAll("[^A-Z0-9]+", "-").replaceAll("^-+|-+$", "").length()));
    }

    private String shortDigest(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (int i = 0; i < 3; i++) {
                hex.append(String.format("%02X", hash[i]));
            }
            return hex.toString();
        } catch (Exception e) {
            return Integer.toHexString(value.hashCode()).toUpperCase();
        }
    }

    private String buildSlotKey(Long weekDayId, LocalTime startTime, LocalTime endTime, Long academicSpaceId) {
        return String.format("%s_%s_%s_%d", weekDayId, startTime, endTime, academicSpaceId);
    }
}
