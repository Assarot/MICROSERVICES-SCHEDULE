package pe.edu.upeu.microserviceinventory2.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upeu.microserviceinventory2.application.ports.input.StateServicePort;
import pe.edu.upeu.microserviceinventory2.application.ports.output.StatePersistencePort;
import pe.edu.upeu.microserviceinventory2.domain.exception.StateNotFoundException;
import pe.edu.upeu.microserviceinventory2.domain.model.State;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StateService implements StateServicePort {

    private final StatePersistencePort persistencePort;

    @Override
    public State findById(Long id) {
        return persistencePort.findById(id).orElseThrow(() -> new StateNotFoundException(id));
    }

    @Override
    public List<State> findAll() {
        return persistencePort.findAll();
    }

    @Override
    public State save(State state) {
        return persistencePort.save(state);
    }

    @Override
    public State update(Long id, State state) {
        State existing = findById(id);
        existing.setName(state.getName());
        existing.setIsActive(state.getIsActive());
        return persistencePort.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        persistencePort.deleteById(id);
    }
}
