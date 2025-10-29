package pe.edu.upeu.microserviceinventory2.application.ports.input;

import pe.edu.upeu.microserviceinventory2.domain.model.ResourceType;

import java.util.List;

public interface ResourceTypeServicePort {
    ResourceType findById(Long id);
    List<ResourceType> findAll();
    List<ResourceType> findActive();
    List<ResourceType> findByCategoryId(Long categoryId);
    ResourceType save(ResourceType resourceType);
    ResourceType update(Long id, ResourceType resourceType);
    void deleteById(Long id);
}
