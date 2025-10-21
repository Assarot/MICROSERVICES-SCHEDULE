package pe.edu.upeu.microservice_inventory.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceAssignment {
    private Long idResourceAssignment;
    private Long idAcademicSpace;
    private Long idResource;
    private Resource resource;
}
