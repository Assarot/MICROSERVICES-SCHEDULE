package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.external.feign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EnvTypeAcademicSpaceDTO {
    @JsonProperty("id_type_academic_space")
    private Long idTypeAcademicSpace;
    private String name;
    @JsonProperty("is_active")
    private Character isActive;
}
