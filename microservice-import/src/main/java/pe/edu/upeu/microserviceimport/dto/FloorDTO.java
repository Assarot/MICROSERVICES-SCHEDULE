package pe.edu.upeu.microserviceimport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FloorDTO {
    private Long idFloor;
    private Integer floorNumber;
    private Character isActive;
    private BuildingDTO building;
}