package pe.edu.upeu.microserviceimport;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;

public class GenerateAmbientesExcel {
    public static void main(String[] args) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        
        // Sheet 1: Datos Básicos
        Sheet sheet1 = workbook.createSheet("Datos Básicos");
        Row header1 = sheet1.createRow(0);
        String[] headers1 = {"Estado", "EstadoActivo", "TipoAcademico", "TipoActivo", "Edificio", "EdificioActivo", "Piso", "PisoActivo", "EdificioPiso"};
        for (int i = 0; i < headers1.length; i++) header1.createCell(i).setCellValue(headers1[i]);
        
        String[][] basicData = {
            {"Disponible", "A", "Aula", "A", "Pabellón A", "A", "1", "A", "Pabellón A"},
            {"Ocupado", "A", "Laboratorio", "A", "Pabellón B", "A", "2", "A", "Pabellón A"},
            {"Mantenimiento", "A", "Taller", "A", "Pabellón C", "A", "3", "A", "Pabellón A"},
            {"", "", "Laboratorio de Redes", "A", "", "", "1", "A", "Pabellón B"},
            {"", "", "Laboratorio de Software", "A", "", "", "2", "A", "Pabellón B"},
            {"", "", "Aula Magna", "A", "", "", "3", "A", "Pabellón B"},
            {"", "", "Laboratorio de Cómputo", "A", "", "", "1", "A", "Pabellón C"},
            {"", "", "Biblioteca", "A", "", "", "2", "A", "Pabellón C"},
            {"", "", "Auditorio", "A", "", "", "", "", ""},
            {"", "", "Gimnasio", "A", "", "", "", "", ""}
        };
        for (int i = 0; i < basicData.length; i++) {
            Row row = sheet1.createRow(i + 1);
            for (int j = 0; j < basicData[i].length; j++) {
                if (!basicData[i][j].isEmpty()) {
                    row.createCell(j).setCellValue(basicData[i][j]);
                }
            }
        }
        
        // Sheet 2: Ambientes Académicos
        Sheet sheet2 = workbook.createSheet("Ambientes Académicos");
        Row header2 = sheet2.createRow(0);
        String[] headers2 = {"Espacio", "Observacion", "Ubicacion", "Capacidad", "Estado", "Tipo", "Edificio", "Piso"};
        for (int i = 0; i < headers2.length; i++) header2.createCell(i).setCellValue(headers2[i]);
        
        String[][] ambientes = {
            {"AULA 101", "Proyector HD y pizarra digital", "Planta baja - Ala Norte", "45", "Disponible", "Aula", "Pabellón A", "1"},
            {"AULA 102", "Pizarra tradicional y ventiladores", "Planta baja - Ala Sur", "40", "Disponible", "Aula", "Pabellón A", "1"},
            {"AULA 103", "Aula pequeña para seminarios", "Planta baja - Ala Este", "25", "Disponible", "Aula", "Pabellón A", "1"},
            {"AULA 104", "Aula con aire acondicionado", "Planta baja - Ala Oeste", "35", "Disponible", "Aula", "Pabellón A", "1"},
            {"LAB 201", "30 computadoras i7 + proyector", "Segundo piso - Laboratorio principal", "30", "Disponible", "Laboratorio", "Pabellón A", "2"},
            {"LAB 202", "25 computadoras i5 + impresora", "Segundo piso - Laboratorio secundario", "25", "Disponible", "Laboratorio de Redes", "Pabellón A", "2"},
            {"LAB 203", "20 workstations para desarrollo", "Segundo piso - Laboratorio de software", "20", "Disponible", "Laboratorio de Software", "Pabellón A", "2"},
            {"AULA 301", "Seminarios y conferencias", "Tercer piso - Sala de reuniones", "50", "Disponible", "Aula Magna", "Pabellón A", "3"},
            {"TALLER 302", "Electrónica y soldadura", "Tercer piso - Taller técnico", "15", "Ocupado", "Taller", "Pabellón A", "3"},
            {"LAB 101", "Computadoras básicas", "Primer piso - Sala de cómputo", "30", "Disponible", "Laboratorio de Cómputo", "Pabellón B", "1"},
            {"BIB 201", "Biblioteca central con zona de estudio", "Segundo piso - Biblioteca", "100", "Disponible", "Biblioteca", "Pabellón B", "2"},
            {"AUD 301", "Auditorio principal con escenario", "Tercer piso - Auditorio", "200", "Disponible", "Auditorio", "Pabellón B", "3"},
            {"GIM 101", "Cancha polideportiva y vestuarios", "Planta baja - Gimnasio", "150", "Disponible", "Gimnasio", "Pabellón C", "1"},
            {"AULA 501", "Aula administrativa", "Quinto piso - Administración", "30", "Disponible", "Aula", "Pabellón C", "2"}
        };
        for (int i = 0; i < ambientes.length; i++) {
            Row row = sheet2.createRow(i + 1);
            for (int j = 0; j < ambientes[i].length; j++) {
                if (j == 3 || j == 7) { // Integer columns
                    row.createCell(j).setCellValue(Integer.parseInt(ambientes[i][j]));
                } else {
                    row.createCell(j).setCellValue(ambientes[i][j]);
                }
            }
        }
        
        try (FileOutputStream fileOut = new FileOutputStream("D:\\Proyectos\\MICROSERVICES-SCHEDULE\\ambientes_academicos_generado.xlsx")) {
            workbook.write(fileOut);
        }
        workbook.close();
        System.out.println("Excel file generated at: D:\\Proyectos\\MICROSERVICES-SCHEDULE\\ambientes_academicos_generado.xlsx");
    }
}
