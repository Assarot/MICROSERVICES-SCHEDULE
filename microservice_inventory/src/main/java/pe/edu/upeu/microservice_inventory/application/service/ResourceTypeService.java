package pe.edu.upeu.microservice_inventory.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.microservice_inventory.domain.model.ResourceType;
import pe.edu.upeu.microservice_inventory.domain.port.in.ResourceTypeUseCase;
import pe.edu.upeu.microservice_inventory.domain.port.out.CategoryResourceRepositoryPort;
import pe.edu.upeu.microservice_inventory.domain.port.out.ResourceTypeRepositoryPort;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ResourceTypeService implements ResourceTypeUseCase {
    
    private final ResourceTypeRepositoryPort resourceTypeRepositoryPort;
    private final CategoryResourceRepositoryPort categoryResourceRepositoryPort;

    @Override
    public ResourceType createResourceType(ResourceType resourceType) {
        if (resourceType.getIdCategoryResource() != null) {
            if (!categoryResourceRepositoryPort.existsById(resourceType.getIdCategoryResource())) {
                throw new RuntimeException("CategoryResource not found with id: " + resourceType.getIdCategoryResource());
            }
        }
        return resourceTypeRepositoryPort.save(resourceType);
    }

    @Override
    public ResourceType updateResourceType(Long id, ResourceType resourceType) {
        if (!resourceTypeRepositoryPort.existsById(id)) {
            throw new RuntimeException("ResourceType not found with id: " + id);
        }
        if (resourceType.getIdCategoryResource() != null) {
            if (!categoryResourceRepositoryPort.existsById(resourceType.getIdCategoryResource())) {
                throw new RuntimeException("CategoryResource not found with id: " + resourceType.getIdCategoryResource());
            }
        }
        resourceType.setIdResourceType(id);
        return resourceTypeRepositoryPort.save(resourceType);
    }

    @Override
    public void deleteResourceType(Long id) {
        if (!resourceTypeRepositoryPort.existsById(id)) {
            throw new RuntimeException("ResourceType not found with id: " + id);
        }
        resourceTypeRepositoryPort.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ResourceType> getResourceTypeById(Long id) {
        return resourceTypeRepositoryPort.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceType> getAllResourceTypes() {
        return resourceTypeRepositoryPort.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceType> getActiveResourceTypes() {
        return resourceTypeRepositoryPort.findByIsActive(true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceType> getResourceTypesByCategoryId(Long categoryId) {
        return resourceTypeRepositoryPort.findByCategoryResourceId(categoryId);
    }
}
