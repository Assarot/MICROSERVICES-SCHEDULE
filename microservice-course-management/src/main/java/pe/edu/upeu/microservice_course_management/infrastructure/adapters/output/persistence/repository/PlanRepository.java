package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.entity.PlanEntity;

public interface PlanRepository extends JpaRepository<PlanEntity, Long> {

}
