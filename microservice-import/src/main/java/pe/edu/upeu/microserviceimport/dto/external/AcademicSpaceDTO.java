package pe.edu.upeu.microserviceimport.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AcademicSpaceDTO {
    @JsonProperty("id_academic_space")
    private Long id;
    @JsonProperty("space_name")
    private String spaceName;
    private Integer capacity;
    private TypeAcademicSpaceDTO typeAcademicSpace;
}
