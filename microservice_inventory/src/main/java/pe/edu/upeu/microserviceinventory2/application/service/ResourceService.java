package pe.edu.upeu.microserviceinventory2.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.microserviceinventory2.application.ports.input.ResourceServicePort;
import pe.edu.upeu.microserviceinventory2.application.ports.output.CloudinaryPort;
import pe.edu.upeu.microserviceinventory2.application.ports.output.ResourcePersistencePort;
import pe.edu.upeu.microserviceinventory2.domain.model.Resource;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceService implements ResourceServicePort {

    private final ResourcePersistencePort persistencePort;
    private final CloudinaryPort cloudinaryPort;

    @Override
    public Resource findById(Long id) {
        return persistencePort.findById(id).orElseThrow(() -> new RuntimeException("Resource not found with id=" + id));
    }

    @Override
    public List<Resource> findAll() {
        return persistencePort.findAll();
    }

    @Override
    public List<Resource> findByTypeId(Long typeId) {
        return persistencePort.findByTypeId(typeId);
    }

    @Override
    public List<Resource> findByStateId(Long stateId) {
        return persistencePort.findByStateId(stateId);
    }

    @Override
    public Resource save(Resource resource, MultipartFile photo) {
        if (photo != null && !photo.isEmpty()) {
            String url = cloudinaryPort.uploadImage(photo);
            resource.setResourcePhotoUrl(url);
        }
        return persistencePort.save(resource);
    }

    @Override
    public Resource update(Long id, Resource resource, MultipartFile photo) {
        Resource existing = findById(id);
        existing.setCode(resource.getCode());
        existing.setStock(resource.getStock());
        existing.setObservation(resource.getObservation());
        existing.setIdResourceType(resource.getIdResourceType());
        existing.setIdState(resource.getIdState());
        if (photo != null && !photo.isEmpty()) {
            String url = cloudinaryPort.uploadImage(photo);
            existing.setResourcePhotoUrl(url);
        }
        return persistencePort.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        persistencePort.deleteById(id);
    }
}
