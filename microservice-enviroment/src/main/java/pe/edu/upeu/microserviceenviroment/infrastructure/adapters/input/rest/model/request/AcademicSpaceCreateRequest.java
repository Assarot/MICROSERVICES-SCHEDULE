package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcademicSpaceCreateRequest {
    @NotBlank(message = "Field space_name cannot be empty or null")
    private String spaceName;
    @NotBlank(message = "Field observation cannot be empty or null")
    private String observation;
    @NotBlank(message = "Field location cannot be empty or null")
    private String location;
    @NotNull(message = "Field capacity cannot be empty or null")
    private int capacity;
    @NotNull(message = "Field id_state cannot be empty or null")
    private Long idState;
    @NotNull(message = "Field id_floor cannot be empty or null")
    private Long idFloor;
    @NotNull(message = "Field id_type_academic_space cannot be empty or null")
    private Long idTypeAcademicSpace;
}
