package pe.edu.upeu.microserviceimport.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.microserviceimport.service.ScheduleOrchestratorService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleController {

    private static final Logger log = LoggerFactory.getLogger(ScheduleController.class);

    private final ScheduleOrchestratorService scheduleOrchestratorService;

    public ScheduleController(ScheduleOrchestratorService scheduleOrchestratorService) {
        this.scheduleOrchestratorService = scheduleOrchestratorService;
    }

    /**
     * Endpoint de multipart/form-data. Recibe el archivo Excel cargado de las materias, 
     * procesa docentes e inserta las peticiones de curso (asignaciones) en base de datos.
     */
    @PostMapping("/import")
    public ResponseEntity<?> importScheduleLoad(@RequestParam("file") MultipartFile file) {
        log.info("========== RECIBIDA SOLICITUD DE CARGA ACADEMICA (FASE 1: IMPORTACIÓN) ==========");
        Map<String, Object> response = new HashMap<>();

        try {
            if (!file.getOriginalFilename().endsWith(".xlsx") && !file.getOriginalFilename().endsWith(".xls")) {
                response.put("status", "ERROR");
                response.put("message", "El archivo debe ser Excel (.xlsx o .xls)");
                return ResponseEntity.badRequest().body(response);
            }

            // Procesar el archivo y persistir en BD
            scheduleOrchestratorService.processAcademicLoadOnly(file.getInputStream());

            response.put("status", "SUCCESS");
            response.put("message", "Carga Académica procesada e importada a la base de datos exitosamente.");
            response.put("filename", file.getOriginalFilename());
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            log.error("Error de I/O al procesar archivo: {}", e.getMessage());
            response.put("status", "ERROR");
            response.put("message", "Error al leer el archivo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        } catch (Exception e) {
            log.error("Error en proceso de importación: {}", e.getMessage(), e);
            response.put("status", "ERROR");
            response.put("message", "Error al procesar carga: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint para enviar al motor la instrucción de generar el horario de una sola facultad o escuela específica.
     * Retorna éxito, cantidad agendada y reporte de conflictos.
     * Lee toda la información directamente de la base de datos.
     */
    @PostMapping("/generate")
    public ResponseEntity<?> generateSchedule() {
        log.info("========== RECIBIDA SOLICITUD PARA GENERAR HORARIOS (FASE 2: GENERACIÓN) ==========");
        Map<String, Object> response = new HashMap<>();

        try {
            Map<String, Object> result = scheduleOrchestratorService.autoAssignAllSchedules();

            response.put("status", "SUCCESS");
            response.put("message", "Horarios generados y autoasignados exitosamente utilizando el motor MRV");
            response.put("scheduledBlocks", result.get("scheduledBlocks"));
            response.put("unresolvedConflicts", result.get("unresolvedConflicts"));
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error en proceso de generación de horarios desde BD: {}", e.getMessage(), e);
            response.put("status", "ERROR");
            response.put("message", "Error al generar horarios: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
