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
    private Integer capacity;
    private Long idState;
    private Long idFloor;
    private Long idTypeAcademicSpace;
    private String typeAcademicSpace;
}
