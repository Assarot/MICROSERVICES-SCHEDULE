package pe.edu.upeu.microservice_inventory.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.microservice_inventory.domain.model.ResourceAssignment;
import pe.edu.upeu.microservice_inventory.domain.port.in.ResourceAssignmentUseCase;
import pe.edu.upeu.microservice_inventory.domain.port.out.ResourceAssignmentRepositoryPort;
import pe.edu.upeu.microservice_inventory.domain.port.out.ResourceRepositoryPort;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ResourceAssignmentService implements ResourceAssignmentUseCase {
    
    private final ResourceAssignmentRepositoryPort resourceAssignmentRepositoryPort;
    private final ResourceRepositoryPort resourceRepositoryPort;

    @Override
    public ResourceAssignment createResourceAssignment(ResourceAssignment resourceAssignment) {
        if (resourceAssignment.getIdResource() != null) {
            if (!resourceRepositoryPort.existsById(resourceAssignment.getIdResource())) {
                throw new RuntimeException("Resource not found with id: " + resourceAssignment.getIdResource());
            }
        }
        return resourceAssignmentRepositoryPort.save(resourceAssignment);
    }

    @Override
    public ResourceAssignment updateResourceAssignment(Long id, ResourceAssignment resourceAssignment) {
        if (!resourceAssignmentRepositoryPort.existsById(id)) {
            throw new RuntimeException("ResourceAssignment not found with id: " + id);
        }
        if (resourceAssignment.getIdResource() != null) {
            if (!resourceRepositoryPort.existsById(resourceAssignment.getIdResource())) {
                throw new RuntimeException("Resource not found with id: " + resourceAssignment.getIdResource());
            }
        }
        resourceAssignment.setIdResourceAssignment(id);
        return resourceAssignmentRepositoryPort.save(resourceAssignment);
    }

    @Override
    public void deleteResourceAssignment(Long id) {
        if (!resourceAssignmentRepositoryPort.existsById(id)) {
            throw new RuntimeException("ResourceAssignment not found with id: " + id);
        }
        resourceAssignmentRepositoryPort.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ResourceAssignment> getResourceAssignmentById(Long id) {
        return resourceAssignmentRepositoryPort.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceAssignment> getAllResourceAssignments() {
        return resourceAssignmentRepositoryPort.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceAssignment> getResourceAssignmentsByResourceId(Long resourceId) {
        return resourceAssignmentRepositoryPort.findByResourceId(resourceId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceAssignment> getResourceAssignmentsByAcademicSpaceId(Long academicSpaceId) {
        return resourceAssignmentRepositoryPort.findByAcademicSpaceId(academicSpaceId);
    }
}
