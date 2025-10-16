package pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence.entity.ResourceTypeEntity;

import java.util.List;

@Repository
public interface ResourceTypeJpaRepository extends JpaRepository<ResourceTypeEntity, Long> {
    List<ResourceTypeEntity> findByIsActive(Boolean isActive);
    List<ResourceTypeEntity> findByIdCategoryResource(Long categoryId);
}
