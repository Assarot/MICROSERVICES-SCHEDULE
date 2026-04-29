package pe.edu.upeu.microserviceimport.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import pe.edu.upeu.microserviceimport.dto.CargaPsicoExcelDTO;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ExcelReaderService {

    private static final String[] HEADERS = {
            "FACULTAD", "ESCUELA", "NOMBRE CURSO", "MODO", "CICLO", "GRUPO",
            "PLAN", "CREDITO", "HT", "HP", "TOTAL HORAS", "HORAS LECTIVAS",
            "MODALIDAD", "DOCENTE", "AFORO POR CURSO Y GRUPO", "AMBIENTE ESPECIALIZADO"
    };

    private final DataFormatter dataFormatter = new DataFormatter();

    public List<CargaPsicoExcelDTO> readExcel(InputStream inputStream) throws IOException {
        List<CargaPsicoExcelDTO> cursos = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            validateHeaders(sheet);

            // Comenzar desde la fila 1 (saltando header en fila 0)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row)) {
                    continue;
                }

                try {
                    CargaPsicoExcelDTO curso = mapRowToDTO(row);
                    if (curso != null) {
                        cursos.add(curso);
                    }
                } catch (Exception e) {
                    log.warn("Error al procesar fila {}: {}", i, e.getMessage());
                }
            }
        }

        log.info("Se leyeron {} cursos del Excel", cursos.size());
        return cursos;
    }

    private CargaPsicoExcelDTO mapRowToDTO(Row row) {
        try {
            String nombreCurso = getCellStringValue(row.getCell(2)); // NOMBRE CURSO
            if (nombreCurso == null || nombreCurso.isEmpty()) {
                return null;
            }

            return CargaPsicoExcelDTO.builder()
                    .facultad(getCellStringValue(row.getCell(0)))
                    .escuela(getCellStringValue(row.getCell(1)))
                    .nombreCurso(nombreCurso)
                    .modo(getCellStringValue(row.getCell(3)))
                    .ciclo(getCellIntValue(row.getCell(4)))
                    .grupo(getCellIntValue(row.getCell(5)))
                    .plan(getCellStringValue(row.getCell(6)))
                    .credito(getCellIntValue(row.getCell(7)))
                    .ht(getCellIntValue(row.getCell(8)))
                    .hp(getCellIntValue(row.getCell(9)))
                    .totalHoras(getCellIntValue(row.getCell(10)))
                    .horasLectivas(getCellIntValue(row.getCell(11)))
                    .modalidad(getCellStringValue(row.getCell(12)))
                    .docente(getCellStringValue(row.getCell(13)))
                    .aforoPorCursoGrupo(getCellIntValue(row.getCell(14)))
                    .ambienteEspecializado(getCellStringValue(row.getCell(15)))
                    .build();
        } catch (Exception e) {
            log.error("Error al mapear fila: {}", e.getMessage());
            return null;
        }
    }

    private String getCellStringValue(org.apache.poi.ss.usermodel.Cell cell) {
        if (cell == null) {
            return "";
        }
        return dataFormatter.formatCellValue(cell).trim();
    }

    private Integer getCellIntValue(org.apache.poi.ss.usermodel.Cell cell) {
        if (cell == null) {
            return 0;
        }
        try {
            String value = dataFormatter.formatCellValue(cell).trim();
            if (value.isEmpty()) {
                return 0;
            }
            return (int) Double.parseDouble(value.replace(",", "."));
        } catch (Exception ignored) {
            return 0;
        }
    }

    private void validateHeaders(Sheet sheet) {
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            throw new IllegalArgumentException("El Excel no tiene fila de encabezados");
        }

        for (int i = 0; i < HEADERS.length; i++) {
            String expected = HEADERS[i];
            String actual = getCellStringValue(headerRow.getCell(i));
            if (!expected.equalsIgnoreCase(actual)) {
                log.warn("Encabezado inesperado en columna {}: esperado='{}', actual='{}'", i + 1, expected, actual);
            }
        }
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK && !dataFormatter.formatCellValue(cell).trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
