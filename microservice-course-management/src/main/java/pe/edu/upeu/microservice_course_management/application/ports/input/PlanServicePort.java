package pe.edu.upeu.microservice_course_management.application.ports.input;

import pe.edu.upeu.microservice_course_management.domain.model.Plan;

import java.util.List;

public interface PlanServicePort {
    Plan findById(Long id);
    List<Plan> findAll();
    Plan save(Plan plan);
    Plan update(Long id, Plan plan);
    void deleteById(Long id);
}
