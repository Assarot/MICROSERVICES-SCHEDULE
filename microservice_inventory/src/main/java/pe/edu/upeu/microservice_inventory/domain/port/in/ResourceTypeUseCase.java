package pe.edu.upeu.microservice_inventory.domain.port.in;

import pe.edu.upeu.microservice_inventory.domain.model.ResourceType;

import java.util.List;
import java.util.Optional;

public interface ResourceTypeUseCase {
    ResourceType createResourceType(ResourceType resourceType);
    ResourceType updateResourceType(Long id, ResourceType resourceType);
    void deleteResourceType(Long id);
    Optional<ResourceType> getResourceTypeById(Long id);
    List<ResourceType> getAllResourceTypes();
    List<ResourceType> getActiveResourceTypes();
    List<ResourceType> getResourceTypesByCategoryId(Long categoryId);
}
