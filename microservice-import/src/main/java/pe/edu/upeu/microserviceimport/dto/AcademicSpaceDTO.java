package pe.edu.upeu.microserviceimport.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AcademicSpaceDTO {
    private Long idAcademicSpace;
    private String spaceName;
    private String observation;
    private String location;
    private Integer capacity;
    private StateDTO state;
    private FloorDTO floor;
    private TypeAcademicSpaceDTO typeAcademicSpace;
}

