package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.ResourceTypeEntity;

import java.util.List;

public interface ResourceTypeRepository extends JpaRepository<ResourceTypeEntity, Long> {
    List<ResourceTypeEntity> findByIsActive(Boolean isActive);
    List<ResourceTypeEntity> findByIdCategoryResource(Long idCategoryResource);
}
