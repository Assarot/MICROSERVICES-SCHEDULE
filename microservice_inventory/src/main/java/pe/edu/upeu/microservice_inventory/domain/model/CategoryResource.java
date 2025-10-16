package pe.edu.upeu.microservice_inventory.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResource {
    private Long idCategoryResource;
    private String name;
    private Boolean isActive;
}
