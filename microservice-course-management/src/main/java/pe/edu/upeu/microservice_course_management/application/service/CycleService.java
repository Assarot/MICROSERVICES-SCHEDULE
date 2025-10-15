package pe.edu.upeu.microservice_course_management.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upeu.microservice_course_management.application.ports.input.CycleServicePort;
import pe.edu.upeu.microservice_course_management.application.ports.output.CyclePersistencePort;
import pe.edu.upeu.microservice_course_management.domain.exception.CycleNotFoundException;
import pe.edu.upeu.microservice_course_management.domain.model.Cycle;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CycleService implements CycleServicePort {

    private final CyclePersistencePort persistencePort;

    @Override
    public Cycle findById(Long id) {
        return persistencePort.finById(id)
                .orElseThrow(CycleNotFoundException::new);
    }

    @Override
    public List<Cycle> findAll() {
        return persistencePort.finAll();
    }

    @Override
    public Cycle save(Cycle cycle) {
        return persistencePort.save(cycle);
    }

    @Override
    public Cycle update(Long id, Cycle cycle) {
        return persistencePort.finById(id)
                .map(saveCycle -> {
                    saveCycle.setName(cycle.getName());
                    return persistencePort.save(saveCycle);
                }).orElseThrow(CycleNotFoundException::new);
    }

    @Override
    public void deleteById(Long id) {
        if (persistencePort.finById(id).isEmpty()) {
            throw new CycleNotFoundException();
        }
        persistencePort.deleteById(id);
    }
}
