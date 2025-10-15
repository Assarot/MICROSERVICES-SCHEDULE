package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.response;

import lombok.*;
import pe.edu.upeu.microserviceenviroment.domain.model.AcademicSpace;
import pe.edu.upeu.microserviceenviroment.domain.model.Building;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FloorResponse {
    private Long idFloor;
    private int floorNumber;
    private char isActive;
    private Building building;
}
