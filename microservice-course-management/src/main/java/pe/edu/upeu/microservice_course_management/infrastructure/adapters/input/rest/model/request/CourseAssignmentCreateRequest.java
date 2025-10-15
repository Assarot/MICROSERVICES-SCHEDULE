package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseAssignmentCreateRequest {
    @NotNull(message = "Field IdCourse cannot be null")
    private Long idCourse;
    @NotNull(message = "Field IdTeacher cannot be null")
    private Long idTeacher;
}
