package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.ResourceEntity;

import java.util.List;

public interface ResourceRepository extends JpaRepository<ResourceEntity, Long> {
    List<ResourceEntity> findByIdResourceType(Long idResourceType);
    List<ResourceEntity> findByIdState(Long idState);
}
