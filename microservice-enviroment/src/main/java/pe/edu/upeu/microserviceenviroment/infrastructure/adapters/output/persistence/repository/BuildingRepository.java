package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.entity.BuildingEntity;

public interface BuildingRepository extends JpaRepository<BuildingEntity, Long> {
}
