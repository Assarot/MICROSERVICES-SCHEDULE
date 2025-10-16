package pe.edu.upeu.microservice_inventory.domain.port.in;

import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.microservice_inventory.domain.model.Resource;

import java.util.List;
import java.util.Optional;

public interface ResourceUseCase {
    Resource createResource(Resource resource, MultipartFile photo);
    Resource updateResource(Long id, Resource resource, MultipartFile photo);
    void deleteResource(Long id);
    Optional<Resource> getResourceById(Long id);
    List<Resource> getAllResources();
    List<Resource> getResourcesByTypeId(Long typeId);
    List<Resource> getResourcesByStateId(Long stateId);
}
