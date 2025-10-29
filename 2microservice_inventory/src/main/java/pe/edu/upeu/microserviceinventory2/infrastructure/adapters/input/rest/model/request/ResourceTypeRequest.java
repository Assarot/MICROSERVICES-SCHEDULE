package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResourceTypeRequest {
    @NotBlank
    private String name;
    @NotNull
    private Boolean isActive;
    @NotNull
    private Long idCategoryResource;
}
