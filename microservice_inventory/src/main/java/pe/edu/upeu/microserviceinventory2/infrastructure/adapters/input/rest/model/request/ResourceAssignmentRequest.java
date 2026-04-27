package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResourceAssignmentRequest {
    @NotNull
    private Long idAcademicSpace;
    @NotNull
    private Long idResource;
}
