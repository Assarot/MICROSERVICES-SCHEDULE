package pe.edu.upeu.microserviceimport.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StateCreateRequest {
    @NotBlank(message = "Field name cannot be empty or null")
    private String name;
    @NotNull(message = "Field is_active cannot be empty or null")
    private char isActive;

    public StateCreateRequest() {}

    public StateCreateRequest(String name, char isActive) {
        this.name = name;
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getIsActive() {
        return isActive;
    }

    public void setIsActive(char isActive) {
        this.isActive = isActive;
    }
}