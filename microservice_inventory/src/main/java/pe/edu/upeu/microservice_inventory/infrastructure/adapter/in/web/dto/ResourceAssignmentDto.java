package pe.edu.upeu.microservice_inventory.infrastructure.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class ResourceAssignmentDto {
    private Long idResourceAssignment;
    
    @NotNull(message = "AcademicSpace ID is required")
    private Long idAcademicSpace;
    
    @NotNull(message = "Resource ID is required")
    private Long idResource;
    
    private ResourceDto resource;
}
