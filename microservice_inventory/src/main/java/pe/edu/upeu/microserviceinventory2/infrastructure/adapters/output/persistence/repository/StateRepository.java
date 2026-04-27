package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.StateEntity;

public interface StateRepository extends JpaRepository<StateEntity, Long> {
}
