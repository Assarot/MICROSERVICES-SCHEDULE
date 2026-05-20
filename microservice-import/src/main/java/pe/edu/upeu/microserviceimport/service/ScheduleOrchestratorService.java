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
    private ScheduleClient scheduleClient;

    @Autowired
    private EnvironmentClient environmentClient;

    @Autowired
    private TeacherClient teacherClient;

    @Autowired
    private CourseAssignmentCourseClient courseAssignmentCourseClient;

    @Autowired
    private WeekDayClient weekDayClient;

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

        // Paso 1: Leer Excel
        log.info("Paso 1: Leyendo archivo Excel...");
        List<CargaPsicoExcelDTO> cursos = excelReaderService.readExcel(excelInputStream);
        log.info("✓ {} cursos leídos del Excel", cursos.size());

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

                    if (existingCoursesByCode.containsKey(generatedCode)) {
                        log.info("✓ Curso duplicado omitido: {}", curso.getNombreCurso());
                        continue;
                    }

                    CourseResponseDTO cursoCreado = courseManagementClient.createCourse(courseDTO);
                    cursosCreados++;
                    log.info("✓ Curso creado: {} (ID: {})", curso.getNombreCurso(), cursoCreado.getIdCourse());
                    existingCoursesByCode.put(generatedCode, cursoCreado);

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
                        pe.edu.upeu.microserviceimport.dto.request.CourseToAssignRequest cta = pe.edu.upeu.microserviceimport.dto.request.CourseToAssignRequest.builder()
                                .idCourseAssignment(idCourseAssignment)
                                .capacityRequired(curso.getAforoPorCursoGrupo() != null ? curso.getAforoPorCursoGrupo() : 30)
                                .preferredType(curso.getAmbienteEspecializado())
                                .candidateAcademicSpaceIds(candidateIds)
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
            String key = String.format("CICLO_%d_GRUPO_%d", curso.getCiclo(), curso.getGrupo());
            agrupados.computeIfAbsent(key, k -> new ArrayList<>()).add(curso);
        }

        return agrupados;
    }

    private CreateCourseDTO convertirACreateCourseDTO(CargaPsicoExcelDTO cursoExcel) {
        long groupId = (cursoExcel.getGrupo() != null && cursoExcel.getGrupo() > 0) ? cursoExcel.getGrupo() : 1L;
        return CreateCourseDTO.builder()
                .name(cursoExcel.getNombreCurso())
                .code(generarCodigoCurso(cursoExcel))
            .description(String.format("Ciclo: %d, Grupo: %d, Modalidad: %s, Docente: %s",
                cursoExcel.getCiclo(), cursoExcel.getGrupo(), cursoExcel.getModalidad(), cursoExcel.getDocente()))
            .duration(cursoExcel.getTotalHoras() != null ? cursoExcel.getTotalHoras() : 0)
            .theoreticalHours(cursoExcel.getHt() != null ? cursoExcel.getHt() : 0)
            .practicalHours(cursoExcel.getHp() != null ? cursoExcel.getHp() : 0)
            .totalHours(java.time.Duration.ofHours(cursoExcel.getTotalHoras() != null ? cursoExcel.getTotalHoras() : 0))
                .idCourseType(1L) // Predeterminado
                .idPlan(1L) // Debería ser dinámico según plan
                .idGroup(groupId)
                .build();
    }

    private String generarCodigoCurso(CargaPsicoExcelDTO curso) {
        String slug = slugify(curso.getNombreCurso());
        String digest = shortDigest(buildCourseKey(curso));
        return String.format("PSI-%d-%d-%s-%s",
                curso.getCiclo(),
                curso.getGrupo(),
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
            log.warn("Docente no encontrado en catálogo maestro, auto-creando docente: {}", curso.getDocente());
            TeacherDTO newTeacher = TeacherDTO.builder()
                    .name(curso.getDocente())
                    .lastName("-")
                    .email(curso.getDocente().replaceAll("\\s+", "").toLowerCase() + "@upeu.edu.pe")
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
            if (weekDays != null) {
                for (WeekDayDTO weekDay : weekDays) {
                    if (weekDay.getIdSchedule() != null && weekDay.getName() != null) {
                        weekDayIds.put(weekDay.getIdSchedule(), weekDay.getName().toUpperCase());
                    }
                }
            }
            return weekDayIds;
        } catch (Exception e) {
            log.warn("No se pudieron cargar los días de semana: {}", e.getMessage());
            return new HashMap<>();
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

    private boolean matchesPreference(AcademicSpaceDTO espacio, String preference) {
        return (espacio.getTypeAcademicSpace() != null && containsIgnoreCase(espacio.getTypeAcademicSpace().getName(), preference))
                || containsIgnoreCase(espacio.getObservation(), preference)
                || containsIgnoreCase(espacio.getLocation(), preference)
                || containsIgnoreCase(espacio.getSpaceName(), preference);
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
                String.valueOf(curso.getGrupo()),
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
