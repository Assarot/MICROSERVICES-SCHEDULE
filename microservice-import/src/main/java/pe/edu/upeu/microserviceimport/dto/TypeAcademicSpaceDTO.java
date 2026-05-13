package pe.edu.upeu.microserviceimport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypeAcademicSpaceDTO {
    private Long idTypeAcademicSpace;
    private String name;
    private Character isActive;
}