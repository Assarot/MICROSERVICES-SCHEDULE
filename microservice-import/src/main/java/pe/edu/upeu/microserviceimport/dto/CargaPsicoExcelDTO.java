package pe.edu.upeu.microserviceimport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CargaPsicoExcelDTO {
    private String facultad;
    private String escuela;
    private String nombreCurso;
    private String modo;
    private Integer ciclo;
    private Integer grupo;
    private String plan; // Formato: 2025-1
    private Integer credito;
    private Integer ht; // Horas teóricas
    private Integer hp; // Horas prácticas
    private Integer totalHoras;
    private Integer horasLectivas;
    private String modalidad;
    private String docente;
    private Integer aforoPorCursoGrupo;
    private String ambienteEspecializado;
}
