package pe.edu.upeu.microservice_inventory.infrastructure.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourceDto {
    private Long idResource;
    
    @NotBlank(message = "Code is required")
    private String code;
    
    private Integer stock;
    private String resourcePhotoUrl;
    private String observation;
    private Long idResourceType;
    private Long idState;
    private ResourceTypeDto resourceType;
    private StateDto state;
}
