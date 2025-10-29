package pe.edu.upeu.microserviceinventory2.application.ports.input;

import pe.edu.upeu.microserviceinventory2.domain.model.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ResourceServicePort {
    Resource findById(Long id);
    List<Resource> findAll();
    List<Resource> findByTypeId(Long typeId);
    List<Resource> findByStateId(Long stateId);
    Resource save(Resource resource, MultipartFile photo);
    Resource update(Long id, Resource resource, MultipartFile photo);
    void deleteById(Long id);
}
