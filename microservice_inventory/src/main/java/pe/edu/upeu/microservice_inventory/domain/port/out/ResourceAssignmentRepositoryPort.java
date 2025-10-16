package pe.edu.upeu.microservice_inventory.domain.port.out;

import pe.edu.upeu.microservice_inventory.domain.model.ResourceAssignment;

import java.util.List;
import java.util.Optional;

public interface ResourceAssignmentRepositoryPort {
    ResourceAssignment save(ResourceAssignment resourceAssignment);
    Optional<ResourceAssignment> findById(Long id);
    List<ResourceAssignment> findAll();
    List<ResourceAssignment> findByResourceId(Long resourceId);
    List<ResourceAssignment> findByAcademicSpaceId(Long academicSpaceId);
    void deleteById(Long id);
    boolean existsById(Long id);
}
