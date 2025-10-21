package pe.edu.upeu.microservice_incident.domain.port;

import pe.edu.upeu.microservice_incident.domain.model.Status;

import java.util.List;
import java.util.Optional;

public interface StatusRepository {
    Status save(Status status);
    Optional<Status> findById(Long id);
    List<Status> findAll();
    void deleteById(Long id);
    boolean existsByName(String name);
}
