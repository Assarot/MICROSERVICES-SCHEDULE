package pe.edu.upeu.microservice_incident.domain.port;

import pe.edu.upeu.microservice_incident.domain.model.Incident;

import java.util.List;
import java.util.Optional;

public interface IncidentRepository {
    Incident save(Incident incident);
    Optional<Incident> findById(Long id);
    List<Incident> findAll();
    void deleteById(Long id);
}
