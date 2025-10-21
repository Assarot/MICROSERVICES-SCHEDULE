package pe.edu.upeu.microservice_incident.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.microservice_incident.domain.model.Severity;

public interface SeverityJpaRepository extends JpaRepository<Severity, Long> {
    boolean existsByName(String name);
}
