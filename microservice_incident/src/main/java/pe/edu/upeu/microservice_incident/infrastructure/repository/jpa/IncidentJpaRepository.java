package pe.edu.upeu.microservice_incident.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.microservice_incident.domain.model.Incident;

public interface IncidentJpaRepository extends JpaRepository<Incident, Long> {
}
