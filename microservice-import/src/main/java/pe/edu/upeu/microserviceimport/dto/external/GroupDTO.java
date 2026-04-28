package pe.edu.upeu.microserviceimport.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GroupDTO {
    @JsonProperty("id_group")
    private Long id;
    @JsonProperty("group_number")
    private String groupNumber;
    private Integer capacity;
}
