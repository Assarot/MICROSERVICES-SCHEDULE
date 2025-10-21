package pe.edu.upeu.microservice_inventory.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.microservice_inventory.domain.model.State;
import pe.edu.upeu.microservice_inventory.domain.port.in.StateUseCase;
import pe.edu.upeu.microservice_inventory.domain.port.out.StateRepositoryPort;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class StateService implements StateUseCase {
    
    private final StateRepositoryPort stateRepositoryPort;

    @Override
    public State createState(State state) {
        return stateRepositoryPort.save(state);
    }

    @Override
    public State updateState(Long id, State state) {
        if (!stateRepositoryPort.existsById(id)) {
            throw new RuntimeException("State not found with id: " + id);
        }
        state.setIdState(id);
        return stateRepositoryPort.save(state);
    }

    @Override
    public void deleteState(Long id) {
        if (!stateRepositoryPort.existsById(id)) {
            throw new RuntimeException("State not found with id: " + id);
        }
        stateRepositoryPort.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<State> getStateById(Long id) {
        return stateRepositoryPort.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<State> getAllStates() {
        return stateRepositoryPort.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<State> getActiveStates() {
        return stateRepositoryPort.findByIsActive(true);
    }
}
