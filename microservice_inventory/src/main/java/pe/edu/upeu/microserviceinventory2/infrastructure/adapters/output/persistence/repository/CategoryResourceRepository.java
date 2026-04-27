package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.CategoryResourceEntity;

import java.util.List;

public interface CategoryResourceRepository extends JpaRepository<CategoryResourceEntity, Long> {
    List<CategoryResourceEntity> findByIsActive(Boolean isActive);
}
