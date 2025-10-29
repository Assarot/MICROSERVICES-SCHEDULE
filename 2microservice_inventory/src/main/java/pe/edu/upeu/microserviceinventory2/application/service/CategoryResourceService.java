package pe.edu.upeu.microserviceinventory2.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upeu.microserviceinventory2.application.ports.input.CategoryResourceServicePort;
import pe.edu.upeu.microserviceinventory2.application.ports.output.CategoryResourcePersistencePort;
import pe.edu.upeu.microserviceinventory2.domain.exception.CategoryResourceNotFoundException;
import pe.edu.upeu.microserviceinventory2.domain.model.CategoryResource;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryResourceService implements CategoryResourceServicePort {

    private final CategoryResourcePersistencePort persistencePort;

    @Override
    public CategoryResource findById(Long id) {
        return persistencePort.findById(id).orElseThrow(() -> new CategoryResourceNotFoundException(id));
    }

    @Override
    public List<CategoryResource> findAll() {
        return persistencePort.findAll();
    }

    @Override
    public List<CategoryResource> findActive() {
        return persistencePort.findByIsActive(true);
    }

    @Override
    public CategoryResource save(CategoryResource categoryResource) {
        return persistencePort.save(categoryResource);
    }

    @Override
    public CategoryResource update(Long id, CategoryResource categoryResource) {
        CategoryResource existing = findById(id);
        existing.setName(categoryResource.getName());
        existing.setIsActive(categoryResource.getIsActive());
        return persistencePort.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        persistencePort.deleteById(id);
    }
}
