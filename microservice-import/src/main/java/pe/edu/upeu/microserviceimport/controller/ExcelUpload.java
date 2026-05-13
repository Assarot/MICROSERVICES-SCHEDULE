package pe.edu.upeu.microserviceimport.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.microserviceimport.service.PrimaryDataImportService;
import pe.edu.upeu.microserviceimport.service.ScheduleOrchestratorService;
import pe.edu.upeu.microserviceimport.service.TeacherLoadService;
import pe.edu.upeu.microserviceimport.dto.PrimaryDataImportResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/import")
public class ExcelUpload {

    private static final Logger log = LoggerFactory.getLogger(ExcelUpload.class);

    private final ScheduleOrchestratorService scheduleOrchestratorService;
    private final TeacherLoadService teacherLoadService;
    private final PrimaryDataImportService primaryDataImportService;

    public ExcelUpload(ScheduleOrchestratorService scheduleOrchestratorService,
                       TeacherLoadService teacherLoadService,
                       PrimaryDataImportService primaryDataImportService) {
        this.scheduleOrchestratorService = scheduleOrchestratorService;
        this.teacherLoadService = teacherLoadService;
        this.primaryDataImportService = primaryDataImportService;
    }

    /**
     * Endpoint para subir y procesar archivo Excel de carga psico
     * Crea cursos y asigna horarios automáticamente
     */
    @PostMapping("/upload-carga-psico")
    public ResponseEntity<?> uploadCargaPsico(@RequestParam("file") MultipartFile file) {
        log.info("========== RECIBIDA SOLICITUD DE CARGA PSICO ==========");
        log.info("Archivo: {}, Tamaño: {} bytes", file.getOriginalFilename(), file.getSize());

        Map<String, Object> response = new HashMap<>();

        try {
            // Validar que sea un archivo Excel
            if (!file.getOriginalFilename().endsWith(".xlsx") && !file.getOriginalFilename().endsWith(".xls")) {
                response.put("status", "ERROR");
                response.put("message", "El archivo debe ser Excel (.xlsx o .xls)");
                return ResponseEntity.badRequest().body(response);
            }

            // Procesar el archivo
            scheduleOrchestratorService.processAndCreateSchedules(file.getInputStream());

            response.put("status", "SUCCESS");
            response.put("message", "Carga Psico procesada exitosamente");
            response.put("filename", file.getOriginalFilename());
            response.put("timestamp", System.currentTimeMillis());

            log.info("✓ Procesamiento completado exitosamente");
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            log.error("Error de I/O al procesar archivo: {}", e.getMessage());
            response.put("status", "ERROR");
            response.put("message", "Error al leer el archivo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        } catch (Exception e) {
            log.error("Error en proceso de orquestación: {}", e.getMessage(), e);
            response.put("status", "ERROR");
            response.put("message", "Error al procesar carga: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint para subir Excel desde recursos
     * Para usar en pruebas con el archivo predefinido
     */
    @PostMapping("/process-default-excel")
    public ResponseEntity<?> processDefaultExcel() {
        log.info("========== PROCESANDO EXCEL PREDEFINIDO DE RECURSOS ==========");

        Map<String, Object> response = new HashMap<>();

        try {
            // Obtener el archivo de recursos
            ClassLoader classLoader = getClass().getClassLoader();
            var inputStream = classLoader.getResourceAsStream("carga psico.xlsx");

            if (inputStream == null) {
                response.put("status", "ERROR");
                response.put("message", "Archivo 'carga psico.xlsx' no encontrado en recursos");
                return ResponseEntity.badRequest().body(response);
            }

            // Procesar el archivo
            scheduleOrchestratorService.processAndCreateSchedules(inputStream);

            response.put("status", "SUCCESS");
            response.put("message", "Excel predefinido procesado exitosamente");
            response.put("timestamp", System.currentTimeMillis());

            log.info("✓ Procesamiento del Excel predefinido completado");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al procesar Excel predefinido: {}", e.getMessage(), e);
            response.put("status", "ERROR");
            response.put("message", "Error al procesar: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint para importar maestros desde un Excel separado.
     * El archivo debe contener: nombre, apellido y correo.
     */
    @PostMapping("/upload-teachers")
    public ResponseEntity<?> uploadTeachers(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (!file.getOriginalFilename().endsWith(".xlsx") && !file.getOriginalFilename().endsWith(".xls")) {
                response.put("status", "ERROR");
                response.put("message", "El archivo debe ser Excel (.xlsx o .xls)");
                return ResponseEntity.badRequest().body(response);
            }

            teacherLoadService.cargarMaestros(file.getInputStream());

            response.put("status", "SUCCESS");
            response.put("message", "Docentes importados exitosamente");
            response.put("filename", file.getOriginalFilename());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", "Error al importar docentes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint para subir y procesar archivo Excel de datos primarios
     * Crea estados, tipos de espacios, edificios, pisos y ambientes académicos
     */
    @PostMapping("/upload-primary-data")
    public ResponseEntity<?> uploadPrimaryData(@RequestParam("file") MultipartFile file) {
        log.info("========== RECIBIDA SOLICITUD DE DATOS PRIMARIOS ==========");
        log.info("Archivo: {}, Tamaño: {} bytes", file.getOriginalFilename(), file.getSize());

        try {
            // Validar que sea un archivo Excel
            if (!file.getOriginalFilename().endsWith(".xlsx") && !file.getOriginalFilename().endsWith(".xls")) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", "ERROR");
                errorResponse.put("message", "El archivo debe ser Excel (.xlsx o .xls)");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Procesar el archivo
            PrimaryDataImportResult result = primaryDataImportService.importPrimaryData(file);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("message", result.getMessage());
            response.put("statesCreated", result.getStatesCreated());
            response.put("typesCreated", result.getTypesCreated());
            response.put("buildingsCreated", result.getBuildingsCreated());
            response.put("floorsCreated", result.getFloorsCreated());
            response.put("academicSpacesCreated", result.getAcademicSpacesCreated());

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            log.error("Error al procesar archivo: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "ERROR");
            errorResponse.put("message", "Error al procesar el archivo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (Exception e) {
            log.error("Error en importación de datos primarios: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "ERROR");
            errorResponse.put("message", "Error en importación: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Health check del servicio
     */
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "microservice-import");
        response.put("message", "Microservicio de importación y orquestación de horarios activo");
        return ResponseEntity.ok(response);
    }
}
