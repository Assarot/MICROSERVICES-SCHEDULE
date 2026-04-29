package pe.edu.upeu.microserviceimport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseDTO {
    private String name;
    private String code;
    private String description;
    private Integer duration;
    private Integer theoreticalHours;
    private Integer practicalHours;
    private Duration totalHours;
    private Long idCourseType;
    private Long idPlan;
    private Long idGroup;
}
