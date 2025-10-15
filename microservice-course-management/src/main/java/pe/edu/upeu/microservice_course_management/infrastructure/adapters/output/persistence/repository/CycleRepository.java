package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.microservice_course_management.domain.model.Cycle;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.entity.CycleEntity;

public interface CycleRepository extends JpaRepository<CycleEntity, Long> {
}
