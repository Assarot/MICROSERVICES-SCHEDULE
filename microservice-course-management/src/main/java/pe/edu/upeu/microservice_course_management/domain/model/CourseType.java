package pe.edu.upeu.microservice_course_management.domain.model;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseType {
    private Long idCourseType;
    private String name;
}
