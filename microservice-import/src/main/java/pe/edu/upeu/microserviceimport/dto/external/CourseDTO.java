package pe.edu.upeu.microserviceimport.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CourseDTO {
    @JsonProperty("id_course")
    private Long idCourse;
    private String name;
    private String code;
    private Integer duration;
    @JsonProperty("theoretical_hours")
    private Integer theoreticalHours;
    @JsonProperty("practical_hours")
    private Integer practicalHours;
    @JsonProperty("total_hours")
    private Integer totalHours;
}
