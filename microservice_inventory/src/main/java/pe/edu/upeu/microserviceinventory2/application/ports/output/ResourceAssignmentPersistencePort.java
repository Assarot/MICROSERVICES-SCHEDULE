package pe.edu.upeu.microserviceinventory2.application.ports.output;

import pe.edu.upeu.microserviceinventory2.domain.model.ResourceAssignment;

import java.util.List;
import java.util.Optional;

public interface ResourceAssignmentPersistencePort {
    Optional<ResourceAssignment> findById(Long id);
    List<ResourceAssignment> findAll();
    List<ResourceAssignment> findByResourceId(Long resourceId);
    List<ResourceAssignment> findByAcademicSpaceId(Long academicSpaceId);
    ResourceAssignment save(ResourceAssignment resourceAssignment);
    void deleteById(Long id);
}
