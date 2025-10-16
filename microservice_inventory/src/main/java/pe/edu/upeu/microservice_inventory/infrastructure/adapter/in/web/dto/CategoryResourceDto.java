package pe.edu.upeu.microservice_inventory.infrastructure.adapter.in.web.dto;

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
public class CategoryResourceDto {
    private Long idCategoryResource;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotNull(message = "IsActive is required")
    private Boolean isActive;
}
