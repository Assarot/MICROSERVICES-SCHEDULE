package pe.edu.upeu.microservice_course_management.domain.model;

import lombok.*;

import java.time.Duration;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    private Long idCourse;
    private String name;
    private String code;
    private String description;
    private Duration duration;
    private Duration theoreticalHours;
    private Duration practicalHours;
    private Duration totalHours;
    private CourseType courseType;
    private Plan plan;
    private Group group;
}
