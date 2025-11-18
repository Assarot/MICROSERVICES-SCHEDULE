package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.external.feign.dto;

import lombok.Data;

@Data
public class EnvBuildingDTO {
    private Long idBuilding;
    private String name;
    private Character isActive;
}
