package pe.edu.upeu.microserviceinventory2.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upeu.microserviceinventory2.application.ports.input.ResourceTypeServicePort;
import pe.edu.upeu.microserviceinventory2.application.ports.output.ResourceTypePersistencePort;
import pe.edu.upeu.microserviceinventory2.domain.exception.ResourceTypeNotFoundException;
import pe.edu.upeu.microserviceinventory2.domain.model.ResourceType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceTypeService implements ResourceTypeServicePort {

    private final ResourceTypePersistencePort persistencePort;

    @Override
    public ResourceType findById(Long id) {
        return persistencePort.findById(id).orElseThrow(() -> new ResourceTypeNotFoundException(id));
    }

    @Override
    public List<ResourceType> findAll() {
        return persistencePort.findAll();
    }

    @Override
    public List<ResourceType> findActive() {
        return persistencePort.findByIsActive(true);
    }

    @Override
    public List<ResourceType> findByCategoryId(Long categoryId) {
        return persistencePort.findByCategoryId(categoryId);
    }

    @Override
    public ResourceType save(ResourceType resourceType) {
        return persistencePort.save(resourceType);
    }

    @Override
    public ResourceType update(Long id, ResourceType resourceType) {
        ResourceType existing = findById(id);
        existing.setName(resourceType.getName());
        existing.setIsActive(resourceType.getIsActive());
        existing.setIdCategoryResource(resourceType.getIdCategoryResource());
        return persistencePort.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        persistencePort.deleteById(id);
    }
}
