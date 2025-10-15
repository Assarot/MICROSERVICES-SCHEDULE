package pe.edu.upeu.microservice_course_management.application.ports.output;

import pe.edu.upeu.microservice_course_management.domain.model.Plan;

import java.util.List;
import java.util.Optional;

public interface PlanPersistencePort {
    Optional<Plan> findById(long id);
    List<Plan> findAll();
    Plan save(Plan plan);
    void deleteById(Long id);
}
