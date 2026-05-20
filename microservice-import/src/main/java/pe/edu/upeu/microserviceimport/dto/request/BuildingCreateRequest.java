package pe.edu.upeu.microserviceimport.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BuildingCreateRequest {
    @NotBlank(message = "Field name cannot be empty or null")
    private String name;
    @NotNull(message = "Field is_active cannot be empty or null")
    private Character isActive;

    public BuildingCreateRequest() {}

    public BuildingCreateRequest(String name, Character isActive) {
        this.name = name;
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Character getIsActive() {
        return isActive;
    }

    public void setIsActive(Character isActive) {
        this.isActive = isActive;
    }
}
