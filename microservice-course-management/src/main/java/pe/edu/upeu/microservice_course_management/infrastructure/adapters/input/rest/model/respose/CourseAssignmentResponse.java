package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.respose;

import lombok.*;
import pe.edu.upeu.microservice_course_management.domain.model.Course;
import pe.edu.upeu.microservice_course_management.domain.model.Teacher;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseAssignmentResponse {
    private Long idAssignment;
    private Course course;
    private Teacher teacher;
}
