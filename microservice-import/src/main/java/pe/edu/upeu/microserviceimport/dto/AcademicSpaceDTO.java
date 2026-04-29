package pe.edu.upeu.microserviceimport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicSpaceDTO {
    private Long idAcademicSpace;
    private String spaceName;
    private String observation;
    private String location;
    private int capacity;
    private String typeAcademicSpace;
}
