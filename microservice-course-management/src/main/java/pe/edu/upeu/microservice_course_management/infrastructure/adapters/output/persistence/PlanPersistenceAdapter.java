package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_course_management.application.ports.output.PlanPersistencePort;
import pe.edu.upeu.microservice_course_management.domain.model.Plan;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.mapper.PlanPersistenceMapper;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.repository.PlanRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlanPersistenceAdapter implements PlanPersistencePort {

    private final PlanRepository repository;
    private final PlanPersistenceMapper mapper;

    @Override
    public Optional<Plan> findById(long id) {
        return repository.findById(id)
                .map(mapper::toPlan);
    }

    @Override
    public List<Plan> findAll() {
        return mapper.toPlanList(repository.findAll());
    }

    @Override
    public Plan save(Plan plan) {
        return mapper.toPlan(repository.save(mapper.toPlanEntity(plan)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
