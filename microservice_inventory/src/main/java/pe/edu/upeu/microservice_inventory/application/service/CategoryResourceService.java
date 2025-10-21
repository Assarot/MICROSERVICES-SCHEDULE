package pe.edu.upeu.microservice_inventory.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.microservice_inventory.domain.model.CategoryResource;
import pe.edu.upeu.microservice_inventory.domain.port.in.CategoryResourceUseCase;
import pe.edu.upeu.microservice_inventory.domain.port.out.CategoryResourceRepositoryPort;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryResourceService implements CategoryResourceUseCase {
    
    private final CategoryResourceRepositoryPort categoryResourceRepositoryPort;

    @Override
    public CategoryResource createCategoryResource(CategoryResource categoryResource) {
        return categoryResourceRepositoryPort.save(categoryResource);
    }

    @Override
    public CategoryResource updateCategoryResource(Long id, CategoryResource categoryResource) {
        if (!categoryResourceRepositoryPort.existsById(id)) {
            throw new RuntimeException("CategoryResource not found with id: " + id);
        }
        categoryResource.setIdCategoryResource(id);
        return categoryResourceRepositoryPort.save(categoryResource);
    }

    @Override
    public void deleteCategoryResource(Long id) {
        if (!categoryResourceRepositoryPort.existsById(id)) {
            throw new RuntimeException("CategoryResource not found with id: " + id);
        }
        categoryResourceRepositoryPort.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryResource> getCategoryResourceById(Long id) {
        return categoryResourceRepositoryPort.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResource> getAllCategoryResources() {
        return categoryResourceRepositoryPort.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResource> getActiveCategoryResources() {
        return categoryResourceRepositoryPort.findByIsActive(true);
    }
}
