package pe.edu.upeu.microservice_course_management.domain.model;

import lombok.*;

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
    private int duration;
    private int creditValue;
    private int theoreticalHours;
    private int practicalHours;
    private CourseType courseType;
    private CourseMode courseMode;
    private Plan plan;
    private Group group;

}
