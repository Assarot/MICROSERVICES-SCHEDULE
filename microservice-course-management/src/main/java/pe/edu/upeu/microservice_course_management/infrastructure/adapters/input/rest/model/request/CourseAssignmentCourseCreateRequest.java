package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseAssignmentCourseCreateRequest {
    @NotNull(message = "Field IdCourse cannot be empty or null")
    private Long idCourse;
    @NotNull(message = "Field IdCourseAssignment cannot be empty or null")
    private Long idCourseAssignment;
}
