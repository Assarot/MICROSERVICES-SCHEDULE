package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.response;

import lombok.Data;

@Data
public class CategoryResourceResponse {
    private Long idCategoryResource;
    private String name;
    private Boolean isActive;
}
