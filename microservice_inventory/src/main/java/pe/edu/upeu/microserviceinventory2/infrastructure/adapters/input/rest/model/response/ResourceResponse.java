package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.response;

import lombok.Data;
import pe.edu.upeu.microserviceinventory2.domain.model.ResourceType;
import pe.edu.upeu.microserviceinventory2.domain.model.State;

@Data
public class ResourceResponse {
    private Long idResource;
    private String code;
    private Integer stock;
    private String resourcePhotoUrl;
    private String observation;
    private ResourceType resourceType;
    private State state;
}
