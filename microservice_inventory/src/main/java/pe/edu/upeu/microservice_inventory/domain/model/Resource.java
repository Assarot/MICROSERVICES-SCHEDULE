package pe.edu.upeu.microservice_inventory.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    private Long idResource;
    private String code;
    private Integer stock;
    private String resourcePhotoUrl;
    private String observation;
    private Long idResourceType;
    private Long idState;
    private ResourceType resourceType;
    private State state;
}
