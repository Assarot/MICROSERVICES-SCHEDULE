package pe.edu.upeu.microservice_incident.domain.port;

import pe.edu.upeu.microservice_incident.domain.model.Severity;

import java.util.List;
import java.util.Optional;

public interface SeverityRepository {
    Severity save(Severity severity);
    Optional<Severity> findById(Long id);
    List<Severity> findAll();
    void deleteById(Long id);
    boolean existsByName(String name);
}
