package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.external.feign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EnvAcademicSpaceDTO {
    @JsonProperty("id_academic_space")
    private Long idAcademicSpace;
    @JsonProperty("space_name")
    private String spaceName;
    private Integer capacity;
    @JsonProperty("type_academic_space")
    private EnvTypeAcademicSpaceDTO typeAcademicSpace;
    private EnvFloorDTO floor;
}
