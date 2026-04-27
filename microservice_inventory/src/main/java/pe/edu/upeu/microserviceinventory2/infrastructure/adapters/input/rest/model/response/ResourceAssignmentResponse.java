package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.response;

import lombok.Data;
import pe.edu.upeu.microserviceinventory2.domain.model.Resource;

@Data
public class ResourceAssignmentResponse {
    private Long idResourceAssignment;
    private Long idAcademicSpace;
    private Resource resource;
}
