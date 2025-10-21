package pe.edu.upeu.microservice_inventory.domain.port.in;

import pe.edu.upeu.microservice_inventory.domain.model.ResourceAssignment;

import java.util.List;
import java.util.Optional;

public interface ResourceAssignmentUseCase {
    ResourceAssignment createResourceAssignment(ResourceAssignment resourceAssignment);
    ResourceAssignment updateResourceAssignment(Long id, ResourceAssignment resourceAssignment);
    void deleteResourceAssignment(Long id);
    Optional<ResourceAssignment> getResourceAssignmentById(Long id);
    List<ResourceAssignment> getAllResourceAssignments();
    List<ResourceAssignment> getResourceAssignmentsByResourceId(Long resourceId);
    List<ResourceAssignment> getResourceAssignmentsByAcademicSpaceId(Long academicSpaceId);
}
