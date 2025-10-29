package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FloorResponse {
    private Long idFloor;
    private int floorNumber;
    private char isActive;
    private BuildingResponse building;
}
