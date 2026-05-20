package pe.edu.upeu.microserviceimport.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FloorCreateRequest {
    @NotNull(message = "Field floor_number cannot be empty or null")
    private int floorNumber;
    @NotNull(message = "Field is_active cannot be empty or null")
    private Character isActive;
    @NotNull(message = "Field id_building cannot be empty or null")
    private Long idBuilding;

    public FloorCreateRequest() {}

    public FloorCreateRequest(int floorNumber, Character isActive, Long idBuilding) {
        this.floorNumber = floorNumber;
        this.isActive = isActive;
        this.idBuilding = idBuilding;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public Character getIsActive() {
        return isActive;
    }

    public void setIsActive(Character isActive) {
        this.isActive = isActive;
    }

    public Long getIdBuilding() {
        return idBuilding;
    }

    public void setIdBuilding(Long idBuilding) {
        this.idBuilding = idBuilding;
    }
}
