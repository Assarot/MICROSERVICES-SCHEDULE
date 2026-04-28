package pe.edu.upeu.microserviceimport.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TeacherDTO {
    @JsonProperty("id_teacher")
    private Long id;
    private String name;
    @JsonProperty("last_name")
    private String lastName;
    private String email;
}
