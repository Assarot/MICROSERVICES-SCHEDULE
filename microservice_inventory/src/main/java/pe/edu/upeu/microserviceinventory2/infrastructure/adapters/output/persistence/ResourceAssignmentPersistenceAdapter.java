package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microserviceinventory2.application.ports.output.ResourceAssignmentPersistencePort;
import pe.edu.upeu.microserviceinventory2.domain.model.ResourceAssignment;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.mapper.ResourceAssignmentPersistenceMapper;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.repository.ResourceAssignmentRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ResourceAssignmentPersistenceAdapter implements ResourceAssignmentPersistencePort {

    private final ResourceAssignmentRepository repository;
    private final ResourceAssignmentPersistenceMapper mapper;

    @Override
    public Optional<ResourceAssignment> findById(Long id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<ResourceAssignment> findAll() {
        return mapper.toModelList(repository.findAll());
    }

    @Override
    public List<ResourceAssignment> findByResourceId(Long resourceId) {
        return mapper.toModelList(repository.findByIdResource(resourceId));
    }

    @Override
    public List<ResourceAssignment> findByAcademicSpaceId(Long academicSpaceId) {
        return mapper.toModelList(repository.findByIdAcademicSpace(academicSpaceId));
    }

    @Override
    public ResourceAssignment save(ResourceAssignment resourceAssignment) {
        var saved = repository.save(mapper.toEntity(resourceAssignment));
        return mapper.toModel(saved);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
