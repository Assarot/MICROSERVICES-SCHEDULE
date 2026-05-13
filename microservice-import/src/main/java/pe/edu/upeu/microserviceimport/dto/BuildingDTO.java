package pe.edu.upeu.microserviceimport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuildingDTO {
    private Long idBuilding;
    private String name;
    private Character isActive;
}