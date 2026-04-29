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
        Map<String, TeacherDTO> teachersByEmail = loadExistingTeachersByEmail();

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

            for (CargaPsicoExcelDTO curso : cursosDelGrupo) {
                try {
                    // Crear curso
                    CreateCourseDTO courseDTO = convertirACreateCourseDTO(curso);
                    CourseResponseDTO cursoCreado = courseManagementClient.createCourse(courseDTO);
                    cursosCreados++;
                    log.info("✓ Curso creado: {} (ID: {})", curso.getNombreCurso(), cursoCreado.getIdCourse());

                    // Obtener o crear asignación de docente
                    Long idCourseAssignment = obtenerOCrearCourseAssignment(curso, cursoCreado.getIdCourse(), teachersByEmail);

                    // Asignar horario sin conflictos
                    boolean horarioAsignado = asignarHorarioSinConflictos(
                            cursoCreado,
                            idCourseAssignment,
                            espacios,
                            scheduleTracker,
                        curso,
                        weekDayIds
                    );

                    if (horarioAsignado) {
                        horariosAsignados++;
                    }

                } catch (Exception e) {
                    log.error("Error al procesar curso {}: {}", curso.getNombreCurso(), e.getMessage());
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
                .idGroup(Long.valueOf(cursoExcel.getGrupo()))
                .build();
    }

    private String generarCodigoCurso(CargaPsicoExcelDTO curso) {
        return String.format("PSI-%d-%d-%s",
                curso.getCiclo(),
                curso.getGrupo(),
                curso.getNombreCurso().substring(0, Math.min(3, curso.getNombreCurso().length())).toUpperCase()
        );
    }

    private Long obtenerOCrearCourseAssignment(CargaPsicoExcelDTO curso, Long idCourse, Map<String, TeacherDTO> teachersByEmail) {
        if (curso.getDocente() == null || curso.getDocente().isBlank()) {
            log.warn("Curso {} sin docente asignado", curso.getNombreCurso());
            return null;
        }

        String email = generarEmailDocente(curso.getDocente());
        TeacherDTO teacher = teachersByEmail.get(email.toLowerCase());

        if (teacher == null) {
            String[] nombreParts = curso.getDocente().trim().split("\\s+");
            String primerNombre = nombreParts.length > 0 ? nombreParts[0] : "Docente";
            String apellido = nombreParts.length > 1 ? String.join(" ", Arrays.copyOfRange(nombreParts, 1, nombreParts.length)) : "Sin Apellido";

            TeacherDTO teacherRequest = TeacherDTO.builder()
                    .name(primerNombre)
                    .lastName(apellido)
                    .email(email)
                    .build();

            teacher = teacherClient.createTeacher(teacherRequest);
            teachersByEmail.put(email.toLowerCase(), teacher);
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

    private String generarEmailDocente(String nombreDocente) {
        return nombreDocente.toLowerCase()
                .replaceAll("\\s+", ".")
                .replaceAll("[áéíóú]", "a") + "@upeu.edu.pe";
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

    private Map<String, TeacherDTO> loadExistingTeachersByEmail() {
        try {
            List<TeacherDTO> teachers = teacherClient.getAllTeachers();
            if (teachers == null) {
                return new HashMap<>();
            }

            return teachers.stream()
                    .filter(teacher -> teacher.getEmail() != null)
                    .collect(Collectors.toMap(
                            teacher -> teacher.getEmail().toLowerCase(),
                            teacher -> teacher,
                            (existing, replacement) -> existing,
                            HashMap::new));
        } catch (Exception e) {
            log.warn("No se pudieron cargar docentes existentes: {}", e.getMessage());
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
        return containsIgnoreCase(espacio.getTypeAcademicSpace(), preference)
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

    private String buildSlotKey(Long weekDayId, LocalTime startTime, LocalTime endTime, Long academicSpaceId) {
        return String.format("%s_%s_%s_%d", weekDayId, startTime, endTime, academicSpaceId);
    }
}
