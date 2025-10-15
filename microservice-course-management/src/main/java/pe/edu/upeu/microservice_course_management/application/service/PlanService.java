package pe.edu.upeu.microservice_course_management.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upeu.microservice_course_management.application.ports.input.PlanServicePort;
import pe.edu.upeu.microservice_course_management.application.ports.output.PlanPersistencePort;
import pe.edu.upeu.microservice_course_management.domain.exception.PlanNotFoundException;
import pe.edu.upeu.microservice_course_management.domain.model.Plan;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService implements PlanServicePort {
    private final PlanPersistencePort persistencePort;

    @Override
    public Plan findById(Long id) {
        return persistencePort.findById(id)
                .orElseThrow(PlanNotFoundException::new);
    }

    @Override
    public List<Plan> findAll() {
        return persistencePort.findAll();
    }

    @Override
    public Plan save(Plan plan) {
        return persistencePort.save(plan);
    }

    @Override
    public Plan update(Long id, Plan plan) {
        return persistencePort.findById(id)
                .map(savePlan -> {
                    savePlan.setName(plan.getName());
                    return persistencePort.save(savePlan);
                }).orElseThrow(PlanNotFoundException::new);
    }

    @Override
    public void deleteById(Long id) {
        if (persistencePort.findById(id).isEmpty()) {
            throw new PlanNotFoundException();
        }

        persistencePort.deleteById(id);

    }
}
