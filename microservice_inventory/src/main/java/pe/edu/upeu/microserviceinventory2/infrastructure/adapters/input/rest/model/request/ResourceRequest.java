package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResourceRequest {
    @NotBlank
    private String code;
    @NotNull
    private Integer stock;
    private String observation;
    @NotNull
    private Long idResourceType;
    @NotNull
    private Long idState;
}
