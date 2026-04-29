package pe.edu.upeu.microserviceimport.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.microserviceimport.client.TeacherClient;
import pe.edu.upeu.microserviceimport.dto.TeacherDTO;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio para cargar docentes desde Excel
 * Este servicio es un complemento del ScheduleOrchestratorService
 * Se puede usar para precarga de docentes si es necesario
 */
@Slf4j
@Service
public class TeacherLoadService {
    
    @Autowired
    private TeacherClient teacherClient;

    /**
     * Carga docentes desde InputStream (Excel)
     * Formato esperado: Nombre | Apellido | Email
     */
    public void cargarMaestros(InputStream is) {
        Map<String, TeacherDTO> existingTeachersByEmail = loadExistingTeachersByEmail();

        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            int docentes = 0;

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    String nombre = getCellValue(row.getCell(0));
                    String apellido = getCellValue(row.getCell(1));
                    String email = getCellValue(row.getCell(2));

                    if (!email.isEmpty()) {
                        TeacherDTO teacher = TeacherDTO.builder()
                                .name(nombre)
                                .lastName(apellido)
                                .email(email)
                                .build();

                        if (!existingTeachersByEmail.containsKey(email.toLowerCase())) {
                            teacherClient.createTeacher(teacher);
                            existingTeachersByEmail.put(email.toLowerCase(), teacher);
                            docentes++;
                            log.info("✓ Docente cargado: {} {}", nombre, apellido);
                        } else {
                            log.info("Docente ya existente, se omite: {}", email);
                        }
                    }
                } catch (Exception e) {
                    log.warn("Error al procesar fila {}: {}", i, e.getMessage());
                }
            }
            
            log.info("Se cargaron {} docentes", docentes);
        } catch (Exception e) {
            log.error("Error al cargar docentes: {}", e.getMessage());
        }
    }

    private Map<String, TeacherDTO> loadExistingTeachersByEmail() {
        Map<String, TeacherDTO> teachers = new HashMap<>();
        try {
            List<TeacherDTO> existingTeachers = teacherClient.getAllTeachers();
            if (existingTeachers != null) {
                for (TeacherDTO teacher : existingTeachers) {
                    if (teacher.getEmail() != null) {
                        teachers.put(teacher.getEmail().toLowerCase(), teacher);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("No fue posible cargar docentes existentes: {}", e.getMessage());
        }
        return teachers;
    }

    private String getCellValue(org.apache.poi.ss.usermodel.Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
}
