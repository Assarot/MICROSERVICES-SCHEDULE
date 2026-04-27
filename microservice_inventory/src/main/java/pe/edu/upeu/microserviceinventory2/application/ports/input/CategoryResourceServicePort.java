package pe.edu.upeu.microserviceinventory2.application.ports.input;

import pe.edu.upeu.microserviceinventory2.domain.model.CategoryResource;

import java.util.List;

public interface CategoryResourceServicePort {
    CategoryResource findById(Long id);
    List<CategoryResource> findAll();
    List<CategoryResource> findActive();
    CategoryResource save(CategoryResource categoryResource);
    CategoryResource update(Long id, CategoryResource categoryResource);
    void deleteById(Long id);
}
