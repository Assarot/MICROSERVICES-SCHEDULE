package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseModeCreateRequest {
    @NotBlank(message = "Field name cannot be empty or null")
    private String name;
}
