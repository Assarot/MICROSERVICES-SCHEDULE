package pe.edu.upeu.microservice_inventory.infrastructure.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourceTypeDto {
    private Long idResourceType;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotNull(message = "IsActive is required")
    private Boolean isActive;
    
    private Long idCategoryResource;
    private CategoryResourceDto categoryResource;
}
