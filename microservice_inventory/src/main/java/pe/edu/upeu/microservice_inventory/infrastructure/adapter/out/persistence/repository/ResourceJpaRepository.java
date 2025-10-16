package pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence.entity.ResourceEntity;

import java.util.List;

@Repository
public interface ResourceJpaRepository extends JpaRepository<ResourceEntity, Long> {
    List<ResourceEntity> findByIdResourceType(Long typeId);
    List<ResourceEntity> findByIdState(Long stateId);
}
