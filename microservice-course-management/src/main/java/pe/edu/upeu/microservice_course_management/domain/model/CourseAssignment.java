package pe.edu.upeu.microservice_course_management.domain.model;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseAssignment {
    private Long idAssignment;
    private Course course;
    private Teacher teacher;
}
