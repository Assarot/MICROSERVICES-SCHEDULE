package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuildingCreateRequest {
    @NotBlank(message = "Field name cannot be empty or null")
    private String name;
    @NotNull(message = "Field is_active cannot be empty or null")
    private char isActive;
}
