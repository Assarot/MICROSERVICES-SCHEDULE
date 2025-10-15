package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.entity.FloorEntity;

public interface FloorRepository extends JpaRepository<FloorEntity, Long> {
}
