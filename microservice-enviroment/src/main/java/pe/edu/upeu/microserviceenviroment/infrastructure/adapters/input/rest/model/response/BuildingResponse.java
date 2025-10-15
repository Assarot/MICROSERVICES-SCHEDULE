package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.response;

import lombok.*;
import pe.edu.upeu.microserviceenviroment.domain.model.Floor;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuildingResponse {
    private Long idBuilding;
    private String name;
    private char isActive;
}
