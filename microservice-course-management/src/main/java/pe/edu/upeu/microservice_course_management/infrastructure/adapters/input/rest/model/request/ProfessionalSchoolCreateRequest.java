package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfessionalSchoolCreateRequest {
    @NotBlank(message = "Field name cannot be empty or null")
    private String name;

    @NotNull(message = "Field id_faculty cannot be null")
    private Long idFaculty;

}