package pe.edu.upeu.microservice_inventory.domain.port.out;

import pe.edu.upeu.microservice_inventory.domain.model.CategoryResource;

import java.util.List;
import java.util.Optional;

public interface CategoryResourceRepositoryPort {
    CategoryResource save(CategoryResource categoryResource);
    Optional<CategoryResource> findById(Long id);
    List<CategoryResource> findAll();
    List<CategoryResource> findByIsActive(Boolean isActive);
    void deleteById(Long id);
    boolean existsById(Long id);
}
