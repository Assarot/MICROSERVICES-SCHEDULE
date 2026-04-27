package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.external.feign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EnvFloorDTO {
    @JsonProperty("floor_number")
    private Integer floorNumber;
    private EnvBuildingDTO building;
}
