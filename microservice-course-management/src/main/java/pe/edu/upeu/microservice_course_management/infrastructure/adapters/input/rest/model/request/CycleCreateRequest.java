package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CycleCreateRequest {
    @NotBlank(message = "Field name cannot be empty or null")
    private String name;

    @NotNull(message = "Field id_professional_school cannot be null")
    private Long idProfessionalSchool;
}
