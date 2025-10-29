package pe.edu.upeu.microserviceinventory2.application.ports.input;

import pe.edu.upeu.microserviceinventory2.domain.model.ResourceAssignment;

import java.util.List;

public interface ResourceAssignmentServicePort {
    ResourceAssignment findById(Long id);
    List<ResourceAssignment> findAll();
    List<ResourceAssignment> findByResourceId(Long resourceId);
    List<ResourceAssignment> findByAcademicSpaceId(Long academicSpaceId);
    ResourceAssignment save(ResourceAssignment resourceAssignment);
    ResourceAssignment update(Long id, ResourceAssignment resourceAssignment);
    void deleteById(Long id);
}
