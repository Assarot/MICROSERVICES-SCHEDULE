package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FloorCreateRequest {
    @NotNull(message = "Field floor_number cannot be empty or null")
    private int floorNumber;
    @NotNull(message = "Field is_active cannot be empty or null")
    private char isActive;
    @NotNull(message = "Field id_building cannot be empty or null")
    private Long idBuilding;
}
