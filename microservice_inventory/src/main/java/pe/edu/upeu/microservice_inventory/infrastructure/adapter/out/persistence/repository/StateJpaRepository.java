package pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence.entity.StateEntity;

import java.util.List;

@Repository
public interface StateJpaRepository extends JpaRepository<StateEntity, Long> {
    List<StateEntity> findByIsActive(Boolean isActive);
}
