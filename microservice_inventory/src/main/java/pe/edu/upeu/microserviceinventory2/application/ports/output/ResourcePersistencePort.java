package pe.edu.upeu.microserviceinventory2.application.ports.output;

import pe.edu.upeu.microserviceinventory2.domain.model.Resource;

import java.util.List;
import java.util.Optional;

public interface ResourcePersistencePort {
    Optional<Resource> findById(Long id);
    List<Resource> findAll();
    List<Resource> findByTypeId(Long typeId);
    List<Resource> findByStateId(Long stateId);
    Resource save(Resource resource);
    void deleteById(Long id);
}
