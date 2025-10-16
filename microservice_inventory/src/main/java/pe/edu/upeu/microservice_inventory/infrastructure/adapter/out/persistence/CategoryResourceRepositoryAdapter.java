package pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_inventory.domain.model.CategoryResource;
import pe.edu.upeu.microservice_inventory.domain.port.out.CategoryResourceRepositoryPort;
import pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence.mapper.PersistenceMapper;
import pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence.repository.CategoryResourceJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryResourceRepositoryAdapter implements CategoryResourceRepositoryPort {
    
    private final CategoryResourceJpaRepository categoryResourceJpaRepository;
    private final PersistenceMapper mapper;

    @Override
    public CategoryResource save(CategoryResource categoryResource) {
        var entity = mapper.toCategoryResourceEntity(categoryResource);
        var savedEntity = categoryResourceJpaRepository.save(entity);
        return mapper.toCategoryResourceDomain(savedEntity);
    }

    @Override
    public Optional<CategoryResource> findById(Long id) {
        return categoryResourceJpaRepository.findById(id)
                .map(mapper::toCategoryResourceDomain);
    }

    @Override
    public List<CategoryResource> findAll() {
        return categoryResourceJpaRepository.findAll().stream()
                .map(mapper::toCategoryResourceDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryResource> findByIsActive(Boolean isActive) {
        return categoryResourceJpaRepository.findByIsActive(isActive).stream()
                .map(mapper::toCategoryResourceDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        categoryResourceJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return categoryResourceJpaRepository.existsById(id);
    }
}
