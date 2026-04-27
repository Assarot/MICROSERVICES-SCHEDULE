package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.response;

import lombok.Data;

@Data
public class StateResponse {
    private Long idState;
    private String name;
    private Boolean isActive;
}
