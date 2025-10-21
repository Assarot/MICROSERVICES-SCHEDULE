package pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence.entity.CategoryResourceEntity;

import java.util.List;

@Repository
public interface CategoryResourceJpaRepository extends JpaRepository<CategoryResourceEntity, Long> {
    List<CategoryResourceEntity> findByIsActive(Boolean isActive);
}
