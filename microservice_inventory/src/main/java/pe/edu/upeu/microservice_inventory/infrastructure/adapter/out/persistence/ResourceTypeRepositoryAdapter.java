package pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_inventory.domain.model.ResourceType;
import pe.edu.upeu.microservice_inventory.domain.port.out.ResourceTypeRepositoryPort;
import pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence.mapper.PersistenceMapper;
import pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence.repository.ResourceTypeJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ResourceTypeRepositoryAdapter implements ResourceTypeRepositoryPort {
    
    private final ResourceTypeJpaRepository resourceTypeJpaRepository;
    private final PersistenceMapper mapper;

    @Override
    public ResourceType save(ResourceType resourceType) {
        var entity = mapper.toResourceTypeEntity(resourceType);
        var savedEntity = resourceTypeJpaRepository.save(entity);
        return mapper.toResourceTypeDomain(savedEntity);
    }

    @Override
    public Optional<ResourceType> findById(Long id) {
        return resourceTypeJpaRepository.findById(id)
                .map(mapper::toResourceTypeDomain);
    }

    @Override
    public List<ResourceType> findAll() {
        return resourceTypeJpaRepository.findAll().stream()
                .map(mapper::toResourceTypeDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ResourceType> findByIsActive(Boolean isActive) {
        return resourceTypeJpaRepository.findByIsActive(isActive).stream()
                .map(mapper::toResourceTypeDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ResourceType> findByCategoryResourceId(Long categoryId) {
        return resourceTypeJpaRepository.findByIdCategoryResource(categoryId).stream()
                .map(mapper::toResourceTypeDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        resourceTypeJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return resourceTypeJpaRepository.existsById(id);
    }
}
