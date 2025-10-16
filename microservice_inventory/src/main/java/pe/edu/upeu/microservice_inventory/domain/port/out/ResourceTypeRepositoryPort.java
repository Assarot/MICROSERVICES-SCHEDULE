package pe.edu.upeu.microservice_inventory.domain.port.out;

import pe.edu.upeu.microservice_inventory.domain.model.ResourceType;

import java.util.List;
import java.util.Optional;

public interface ResourceTypeRepositoryPort {
    ResourceType save(ResourceType resourceType);
    Optional<ResourceType> findById(Long id);
    List<ResourceType> findAll();
    List<ResourceType> findByIsActive(Boolean isActive);
    List<ResourceType> findByCategoryResourceId(Long categoryId);
    void deleteById(Long id);
    boolean existsById(Long id);
}
