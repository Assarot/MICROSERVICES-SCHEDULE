package pe.edu.upeu.microservice_inventory.domain.port.in;

import pe.edu.upeu.microservice_inventory.domain.model.CategoryResource;

import java.util.List;
import java.util.Optional;

public interface CategoryResourceUseCase {
    CategoryResource createCategoryResource(CategoryResource categoryResource);
    CategoryResource updateCategoryResource(Long id, CategoryResource categoryResource);
    void deleteCategoryResource(Long id);
    Optional<CategoryResource> getCategoryResourceById(Long id);
    List<CategoryResource> getAllCategoryResources();
    List<CategoryResource> getActiveCategoryResources();
}
