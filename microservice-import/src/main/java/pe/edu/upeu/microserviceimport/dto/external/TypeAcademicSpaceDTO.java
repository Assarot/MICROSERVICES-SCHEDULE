package pe.edu.upeu.microserviceimport.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TypeAcademicSpaceDTO {
    @JsonProperty("id_type_academic_space")
    private Long idTypeAcademicSpace;
    private String name;
    @JsonProperty("is_active")
    private Character isActive;
}
