package pe.edu.upeu.microserviceinventory2.application.ports.output;

import pe.edu.upeu.microserviceinventory2.domain.model.ResourceType;

import java.util.List;
import java.util.Optional;

public interface ResourceTypePersistencePort {
    Optional<ResourceType> findById(Long id);
    List<ResourceType> findAll();
    List<ResourceType> findByIsActive(Boolean isActive);
    List<ResourceType> findByCategoryId(Long categoryId);
    ResourceType save(ResourceType resourceType);
    void deleteById(Long id);
}
