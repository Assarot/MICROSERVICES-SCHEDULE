package pe.edu.upeu.microserviceenviroment.domain.model;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Floor {
    private Long idFloor;
    private int floorNumber;
    private char isActive;
    private Building building;
}
