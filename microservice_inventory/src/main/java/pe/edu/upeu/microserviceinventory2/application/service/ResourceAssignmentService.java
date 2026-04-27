package pe.edu.upeu.microserviceinventory2.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upeu.microserviceinventory2.application.ports.input.ResourceAssignmentServicePort;
import pe.edu.upeu.microserviceinventory2.application.ports.output.ResourceAssignmentPersistencePort;
import pe.edu.upeu.microserviceinventory2.domain.model.ResourceAssignment;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.external.EnvironmentClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceAssignmentService implements ResourceAssignmentServicePort {

    private final ResourceAssignmentPersistencePort persistencePort;
    private final EnvironmentClient environmentClient;

    @Override
    public ResourceAssignment findById(Long id) {
        return persistencePort.findById(id).orElseThrow(() -> new RuntimeException("ResourceAssignment not found with id=" + id));
    }

    @Override
    public List<ResourceAssignment> findAll() {
        return persistencePort.findAll();
    }

    @Override
    public List<ResourceAssignment> findByResourceId(Long resourceId) {
        return persistencePort.findByResourceId(resourceId);
    }

    @Override
    public List<ResourceAssignment> findByAcademicSpaceId(Long academicSpaceId) {
        return persistencePort.findByAcademicSpaceId(academicSpaceId);
    }

    @Override
    public ResourceAssignment save(ResourceAssignment resourceAssignment) {
        validateAcademicSpace(resourceAssignment.getIdAcademicSpace());
        return persistencePort.save(resourceAssignment);
    }

    @Override
    public ResourceAssignment update(Long id, ResourceAssignment resourceAssignment) {
        ResourceAssignment existing = findById(id);
        validateAcademicSpace(resourceAssignment.getIdAcademicSpace());
        existing.setIdAcademicSpace(resourceAssignment.getIdAcademicSpace());
        existing.setIdResource(resourceAssignment.getIdResource());
        return persistencePort.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        persistencePort.deleteById(id);
    }

    private void validateAcademicSpace(Long idAcademicSpace) {
        if (idAcademicSpace == null || !environmentClient.existsAcademicSpace(idAcademicSpace)) {
            throw new IllegalArgumentException("Invalid academic_space id: " + idAcademicSpace);
        }
    }
}
