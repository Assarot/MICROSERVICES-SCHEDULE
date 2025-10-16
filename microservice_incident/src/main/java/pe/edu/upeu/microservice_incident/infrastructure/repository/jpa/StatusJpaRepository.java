package pe.edu.upeu.microservice_incident.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.microservice_incident.domain.model.Status;

public interface StatusJpaRepository extends JpaRepository<Status, Long> {
    boolean existsByName(String name);
}
