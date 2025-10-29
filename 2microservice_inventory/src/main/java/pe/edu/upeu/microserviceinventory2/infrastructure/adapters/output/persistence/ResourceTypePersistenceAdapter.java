package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microserviceinventory2.application.ports.output.ResourceTypePersistencePort;
import pe.edu.upeu.microserviceinventory2.domain.model.ResourceType;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.ResourceTypeEntity;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.mapper.ResourceTypePersistenceMapper;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.repository.ResourceTypeRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ResourceTypePersistenceAdapter implements ResourceTypePersistencePort {

    private final ResourceTypeRepository repository;
    private final ResourceTypePersistenceMapper mapper;

    @Override
    public Optional<ResourceType> findById(Long id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<ResourceType> findAll() {
        return mapper.toModelList(repository.findAll());
    }

    @Override
    public List<ResourceType> findByIsActive(Boolean isActive) {
        return mapper.toModelList(repository.findByIsActive(isActive));
    }

    @Override
    public List<ResourceType> findByCategoryId(Long categoryId) {
        return mapper.toModelList(repository.findByIdCategoryResource(categoryId));
    }

    @Override
    public ResourceType save(ResourceType resourceType) {
        ResourceTypeEntity entity = mapper.toEntity(resourceType);
        ResourceTypeEntity saved = repository.save(entity);
        return mapper.toModel(saved);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
