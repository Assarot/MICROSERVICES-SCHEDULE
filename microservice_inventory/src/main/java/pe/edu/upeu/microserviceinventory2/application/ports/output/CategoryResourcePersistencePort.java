package pe.edu.upeu.microserviceinventory2.application.ports.output;

import pe.edu.upeu.microserviceinventory2.domain.model.CategoryResource;

import java.util.List;
import java.util.Optional;

public interface CategoryResourcePersistencePort {
    Optional<CategoryResource> findById(Long id);
    List<CategoryResource> findAll();
    List<CategoryResource> findByIsActive(Boolean isActive);
    CategoryResource save(CategoryResource categoryResource);
    void deleteById(Long id);
}
