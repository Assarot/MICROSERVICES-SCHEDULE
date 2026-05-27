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

            int headerRowIndex = validateHeaders(sheet);

            // Comenzar desde la fila siguiente al encabezado detectado
            for (int i = headerRowIndex + 1; i <= sheet.getLastRowNum(); i++) {
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

            // Ignorar filas que son títulos de bloque por ciclo (p.ej. "CICLO I", "CICLO II")
            String upperName = nombreCurso.trim().toUpperCase();
            if (upperName.startsWith("CICLO") || upperName.matches("CICLO\\s*[IVXLC]+")) {
                return null;
            }

            // Normalizar nombre eliminando sufijos de grupo/panel como " - P1 - G1", "- G2", " - Teoría G1"
            String cleanedName = normalizeCourseName(nombreCurso);

            int htVal = parseHoursCell(row.getCell(8));
            int hpVal = parseHoursCell(row.getCell(9));
            int totalParsed = parseHoursCell(row.getCell(10));
            int totalFinal = totalParsed;
            int sumHtHp = htVal + hpVal;
            if (sumHtHp > 0) {
                if (totalParsed != sumHtHp) {
                    log.warn("Inconsistencia horas en fila {}: HT+HP={} pero TOTAL_HORAS={} — se usará HT+HP", row.getRowNum() + 1, sumHtHp, totalParsed);
                }
                totalFinal = sumHtHp;
            } else if (totalParsed > 0) {
                totalFinal = totalParsed;
            }

            return CargaPsicoExcelDTO.builder()
                    .facultad(getCellStringValue(row.getCell(0)))
                    .escuela(getCellStringValue(row.getCell(1)))
                    .nombreCurso(cleanedName)
                    .modo(getCellStringValue(row.getCell(3)))
                    .ciclo(parseCicloCell(row.getCell(4)))
                    .grupo(parseGrupoCell(row.getCell(5)))
                    .plan(getCellStringValue(row.getCell(6)))
                    .credito(getCellIntValue(row.getCell(7)))
                    .ht(htVal)
                    .hp(hpVal)
                    .totalHoras(totalFinal)
                    .horasLectivas(getCellIntValue(row.getCell(11)))
                    .modalidad(getCellStringValue(row.getCell(12)))
                    .docente(getCellStringValue(row.getCell(13)))
                    .aforoPorCursoGrupo(getCellIntValue(row.getCell(14)))
                    .ambienteEspecializado(getCellStringValue(row.getCell(15)).isBlank() ? "AULA" : getCellStringValue(row.getCell(15)))
                    .build();
        } catch (Exception e) {
            log.error("Error al mapear fila: {}", e.getMessage());
            return null;
        }
    }

    public String normalizeCourseName(String raw) {
        if (raw == null) return "";
        String s = raw.trim();
        int hyphenIndex = s.indexOf("-");
        if (hyphenIndex != -1) {
            s = s.substring(0, hyphenIndex).trim();
        }

        // Quitar sufijos finales como " G1", "(G2)", " GP 1", " G-1", etc.
        String cleaned = s.replaceAll("\\s*\\(?(?:G|GP)\\s*-?\\s*\\d+\\)?$", "");
        cleaned = cleaned.replaceAll("\\s*GP\\s*\\d+$", "");
        cleaned = cleaned.replaceAll("\\s*G\\s*\\d+$", "");
        cleaned = cleaned.replaceAll("\\s*\\(G\\s*\\d+\\)$", "");
        // Remover palabras sueltas de modalidad que puedan quedar
        cleaned = cleaned.replaceAll("\\bTEORÍA\\b|\\bTEORIA\\b|\\bPRÁCTICA\\b|\\bPRACTICA\\b", "");

        // Quitar sufijos de rol docente que a veces aparecen en el nombre
        cleaned = cleaned.replaceAll("(?i)\\s*-\\s*(TITULAR|JEFE DE PR\\u00C1CTICAS|JEFE DE PRACTICAS|ADJUNTO|ASOCIADO|CONTRATADO)$", "");
        cleaned = cleaned.replaceAll("(?i)\\s*\\(\\s*(TITULAR|JEFE DE PR\\u00C1CTICAS|JEFE DE PRACTICAS|ADJUNTO|ASOCIADO|CONTRATADO)\\s*\\)$", "");

        // Quitar guiones o guiones finales sobrantes
        cleaned = cleaned.replaceAll("[-\\s]+$", "");
        return cleaned.replaceAll("\\s{2,}", " ").trim();
    }

    private Integer parseGrupoCell(Cell cell) {
        String raw = getCellStringValue(cell);
        if (raw == null || raw.isEmpty()) return null; // devolver null si falta grupo
        String up = raw.trim().toUpperCase();
        // Si el contenido indica "UNICO" dejarlo como null para indicar grupo compartido
        if (up.contains("UNICO") || up.contains("ÚNICO") || up.equals("UNICO")) {
            return null;
        }
        // Buscar dígitos dentro del texto
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("(\\d+)").matcher(up);
        if (m.find()) {
            try { return Integer.parseInt(m.group(1)); } catch (Exception ignored) {}
        }
        // Si contiene patrón G\d
        m = java.util.regex.Pattern.compile("G\\s*(\\d+)").matcher(up);
        if (m.find()) {
            try { return Integer.parseInt(m.group(1)); } catch (Exception ignored) {}
        }
        return 1;
    }

    private Integer parseCicloCell(Cell cell) {
        String raw = getCellStringValue(cell);
        if (raw == null || raw.isEmpty()) return 1;
        String up = raw.trim().toUpperCase();

        // Spanish ordinal word mapping
        if (up.contains("PRIMER") || up.contains("PRIME")) return 1;
        if (up.contains("SEGUND")) return 2;
        if (up.contains("TERCER")) return 3;
        if (up.contains("CUART")) return 4;
        if (up.contains("QUINT")) return 5;
        if (up.contains("SEXT")) return 6;
        if (up.contains("SEPTIM") || up.contains("SÉPTIM")) return 7;
        if (up.contains("OCTAV")) return 8;
        if (up.contains("NOVEN")) return 9;
        if (up.contains("DECIM") || up.contains("DÉCIM")) return 10;

        // Buscar dígitos primero
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("(\\d+)").matcher(up);
        if (m.find()) {
            try { return Integer.parseInt(m.group(1)); } catch (Exception ignored) {}
        }
        // Buscar numerales romanos (hasta X)
        java.util.regex.Matcher roman = java.util.regex.Pattern.compile("\\b(X|IX|VIII|VII|VI|V|IV|III|II|I)\\b").matcher(up);
        if (roman.find()) {
            String r = roman.group(0);
            try {
                int v = romanToInt(r);
                if (v > 0) return v;
            } catch (Exception ignored) {}
        }

        return 1;
    }

    private int romanToInt(String s) {
        if (s == null) return 0;
        s = s.toUpperCase().replaceAll("[^IVXLCDM]", "");
        int[] vals = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            switch (s.charAt(i)) {
                case 'I': vals[i] = 1; break;
                case 'V': vals[i] = 5; break;
                case 'X': vals[i] = 10; break;
                case 'L': vals[i] = 50; break;
                case 'C': vals[i] = 100; break;
                case 'D': vals[i] = 500; break;
                case 'M': vals[i] = 1000; break;
                default: vals[i] = 0; break;
            }
        }
        int sum = 0;
        for (int i = 0; i < vals.length; i++) {
            if (i + 1 < vals.length && vals[i] < vals[i+1]) {
                sum -= vals[i];
            } else {
                sum += vals[i];
            }
        }
        return sum;
    }

    private Integer parseHoursCell(Cell cell) {
        String raw = getCellStringValue(cell);
        if (raw == null || raw.isEmpty()) return 0;
        String up = raw.trim().toLowerCase();
        // Formato ejemplo: "0h 3m" o "3m" o "4" (horas)
        try {
            java.util.regex.Matcher m = java.util.regex.Pattern.compile("(?:(\\d+)\\s*h).*(?:(\\d+)\\s*m)?").matcher(up);
            if (m.find()) {
                int h = 0;
                int min = 0;
                if (m.groupCount() >= 1 && m.group(1) != null) h = Integer.parseInt(m.group(1));
                if (m.groupCount() >= 2 && m.group(2) != null) min = Integer.parseInt(m.group(2));
                // Convertir a horas enteras redondeando minutos
                return h + (min >= 30 ? 1 : 0);
            }
            // Si es solo minutos "3m"
            m = java.util.regex.Pattern.compile("(\\d+)\\s*m").matcher(up);
            if (m.find()) {
                int min = Integer.parseInt(m.group(1));
                // Si la cifra es pequeña (p.ej. 1..6) podría estar mal etiquetada como minutos
                if (min > 0 && min <= 6) {
                    return min; // tratar como horas
                }
                // Minutos -> convertir a horas redondeando
                return (min >= 30 ? 1 : 0);
            }
            // Si es número simple (horas)
            m = java.util.regex.Pattern.compile("(\\d+)").matcher(up);
            if (m.find()) {
                int v = Integer.parseInt(m.group(1));
                // Heurística: si el número es mayor que 12 es muy probable que sean minutos (p.ej. 32 -> 32 minutos)
                if (v > 12) {
                    return (v >= 30 ? 1 : 0);
                }
                return v;
            }
        } catch (Exception ignored) {}
        return 0;
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

    private int validateHeaders(Sheet sheet) {
        // Buscar la fila de encabezados dentro de las primeras 8 filas
        int headerRowIndex = -1;
        for (int r = 0; r < Math.min(8, sheet.getLastRowNum() + 1); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            int matches = 0;
            for (int i = 0; i < HEADERS.length; i++) {
                String expected = HEADERS[i];
                String actual = getCellStringValue(row.getCell(i));
                if (actual != null && !actual.isEmpty() && expected.equalsIgnoreCase(actual)) {
                    matches++;
                }
            }
            if (matches >= Math.max(4, HEADERS.length / 2)) {
                headerRowIndex = r;
                break;
            }
        }

        if (headerRowIndex == -1) {
            log.warn("No se detectó una fila de encabezados claramente; se usará la fila 0 por defecto");
            headerRowIndex = 0;
        }

        Row headerRow = sheet.getRow(headerRowIndex);
        for (int i = 0; i < HEADERS.length; i++) {
            String expected = HEADERS[i];
            String actual = getCellStringValue(headerRow.getCell(i));
            if (!expected.equalsIgnoreCase(actual)) {
                log.warn("Encabezado inesperado en fila {} columna {}: esperado='{}', actual='{}'", headerRowIndex + 1, i + 1, expected, actual);
            }
        }

        return headerRowIndex;
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
