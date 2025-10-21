package pe.edu.upeu.microservice_inventory.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.microservice_inventory.domain.model.Resource;
import pe.edu.upeu.microservice_inventory.domain.port.in.ResourceUseCase;
import pe.edu.upeu.microservice_inventory.domain.port.out.CloudinaryPort;
import pe.edu.upeu.microservice_inventory.domain.port.out.ResourceRepositoryPort;
import pe.edu.upeu.microservice_inventory.domain.port.out.ResourceTypeRepositoryPort;
import pe.edu.upeu.microservice_inventory.domain.port.out.StateRepositoryPort;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ResourceService implements ResourceUseCase {
    
    private final ResourceRepositoryPort resourceRepositoryPort;
    private final ResourceTypeRepositoryPort resourceTypeRepositoryPort;
    private final StateRepositoryPort stateRepositoryPort;
    private final CloudinaryPort cloudinaryPort;

    @Override
    public Resource createResource(Resource resource, MultipartFile photo) {
        validateResource(resource);
        
        if (photo != null && !photo.isEmpty()) {
            String photoUrl = cloudinaryPort.uploadImage(photo);
            resource.setResourcePhotoUrl(photoUrl);
        }
        
        return resourceRepositoryPort.save(resource);
    }

    @Override
    public Resource updateResource(Long id, Resource resource, MultipartFile photo) {
        if (!resourceRepositoryPort.existsById(id)) {
            throw new RuntimeException("Resource not found with id: " + id);
        }
        
        validateResource(resource);
        
        if (photo != null && !photo.isEmpty()) {
            String photoUrl = cloudinaryPort.uploadImage(photo);
            resource.setResourcePhotoUrl(photoUrl);
        }
        
        resource.setIdResource(id);
        return resourceRepositoryPort.save(resource);
    }

    @Override
    public void deleteResource(Long id) {
        if (!resourceRepositoryPort.existsById(id)) {
            throw new RuntimeException("Resource not found with id: " + id);
        }
        resourceRepositoryPort.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Resource> getResourceById(Long id) {
        return resourceRepositoryPort.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Resource> getAllResources() {
        return resourceRepositoryPort.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Resource> getResourcesByTypeId(Long typeId) {
        return resourceRepositoryPort.findByResourceTypeId(typeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Resource> getResourcesByStateId(Long stateId) {
        return resourceRepositoryPort.findByStateId(stateId);
    }

    private void validateResource(Resource resource) {
        if (resource.getIdResourceType() != null) {
            if (!resourceTypeRepositoryPort.existsById(resource.getIdResourceType())) {
                throw new RuntimeException("ResourceType not found with id: " + resource.getIdResourceType());
            }
        }
        if (resource.getIdState() != null) {
            if (!stateRepositoryPort.existsById(resource.getIdState())) {
                throw new RuntimeException("State not found with id: " + resource.getIdState());
            }
        }
    }
}
