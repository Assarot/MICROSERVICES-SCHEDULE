package pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_inventory.domain.model.ResourceAssignment;
import pe.edu.upeu.microservice_inventory.domain.port.out.ResourceAssignmentRepositoryPort;
import pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence.mapper.PersistenceMapper;
import pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence.repository.ResourceAssignmentJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ResourceAssignmentRepositoryAdapter implements ResourceAssignmentRepositoryPort {
    
    private final ResourceAssignmentJpaRepository resourceAssignmentJpaRepository;
    private final PersistenceMapper mapper;

    @Override
    public ResourceAssignment save(ResourceAssignment resourceAssignment) {
        var entity = mapper.toResourceAssignmentEntity(resourceAssignment);
        var savedEntity = resourceAssignmentJpaRepository.save(entity);
        return mapper.toResourceAssignmentDomain(savedEntity);
    }

    @Override
    public Optional<ResourceAssignment> findById(Long id) {
        return resourceAssignmentJpaRepository.findById(id)
                .map(mapper::toResourceAssignmentDomain);
    }

    @Override
    public List<ResourceAssignment> findAll() {
        return resourceAssignmentJpaRepository.findAll().stream()
                .map(mapper::toResourceAssignmentDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ResourceAssignment> findByResourceId(Long resourceId) {
        return resourceAssignmentJpaRepository.findByIdResource(resourceId).stream()
                .map(mapper::toResourceAssignmentDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ResourceAssignment> findByAcademicSpaceId(Long academicSpaceId) {
        return resourceAssignmentJpaRepository.findByIdAcademicSpace(academicSpaceId).stream()
                .map(mapper::toResourceAssignmentDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        resourceAssignmentJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return resourceAssignmentJpaRepository.existsById(id);
    }
}
