package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microserviceinventory2.application.ports.output.CategoryResourcePersistencePort;
import pe.edu.upeu.microserviceinventory2.domain.model.CategoryResource;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.CategoryResourceEntity;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.mapper.CategoryResourcePersistenceMapper;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.repository.CategoryResourceRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryResourcePersistenceAdapter implements CategoryResourcePersistencePort {

    private final CategoryResourceRepository repository;
    private final CategoryResourcePersistenceMapper mapper;

    @Override
    public Optional<CategoryResource> findById(Long id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<CategoryResource> findAll() {
        return mapper.toModelList(repository.findAll());
    }

    @Override
    public List<CategoryResource> findByIsActive(Boolean isActive) {
        return mapper.toModelList(repository.findByIsActive(isActive));
    }

    @Override
    public CategoryResource save(CategoryResource categoryResource) {
        CategoryResourceEntity entity = mapper.toEntity(categoryResource);
        CategoryResourceEntity saved = repository.save(entity);
        return mapper.toModel(saved);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
