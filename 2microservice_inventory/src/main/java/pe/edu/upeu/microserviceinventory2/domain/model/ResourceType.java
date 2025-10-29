package pe.edu.upeu.microserviceinventory2.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceType {
    private Long idResourceType;
    private String name;
    private Boolean isActive;
    private Long idCategoryResource;
    private CategoryResource categoryResource;
}
