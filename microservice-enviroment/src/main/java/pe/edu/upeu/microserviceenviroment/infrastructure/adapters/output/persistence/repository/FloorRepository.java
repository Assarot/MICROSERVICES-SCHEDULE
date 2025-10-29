package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.entity.FloorEntity;
import java.util.List;

public interface FloorRepository extends JpaRepository<FloorEntity, Long> {
    List<FloorEntity> findByBuildingEntity_IdBuilding(Long idBuilding);
}
