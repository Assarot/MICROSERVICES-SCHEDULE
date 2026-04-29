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
public class CourseResponseDTO {
    private Long idCourse;
    private String name;
    private String code;
    private String description;
    private Duration duration;
    private Duration theoreticalHours;
    private Duration practicalHours;
    private Duration totalHours;
}