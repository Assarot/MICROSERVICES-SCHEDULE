package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.response;

import lombok.Data;
import pe.edu.upeu.microserviceinventory2.domain.model.CategoryResource;

@Data
public class ResourceTypeResponse {
    private Long idResourceType;
    private String name;
    private Boolean isActive;
    private CategoryResource categoryResource;
}
