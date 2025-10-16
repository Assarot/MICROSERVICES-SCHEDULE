package pe.edu.upeu.microservice_inventory.domain.port.out;

import pe.edu.upeu.microservice_inventory.domain.model.Resource;

import java.util.List;
import java.util.Optional;

public interface ResourceRepositoryPort {
    Resource save(Resource resource);
    Optional<Resource> findById(Long id);
    List<Resource> findAll();
    List<Resource> findByResourceTypeId(Long typeId);
    List<Resource> findByStateId(Long stateId);
    void deleteById(Long id);
    boolean existsById(Long id);
}
