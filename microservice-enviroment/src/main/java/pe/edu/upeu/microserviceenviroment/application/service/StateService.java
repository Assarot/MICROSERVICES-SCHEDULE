package pe.edu.upeu.microserviceenviroment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upeu.microserviceenviroment.application.ports.input.StateServicePort;
import pe.edu.upeu.microserviceenviroment.application.ports.output.StatePersistancePort;
import pe.edu.upeu.microserviceenviroment.domain.exception.StateNotFoundException;
import pe.edu.upeu.microserviceenviroment.domain.model.State;

import java.util.List;
@Service
@RequiredArgsConstructor
public class StateService implements StateServicePort {
    private final StatePersistancePort statePersistancePort;

    @Override
    public State findById(Long id) {
        return statePersistancePort.findById(id)
                .orElseThrow(StackOverflowError::new);
    }

    @Override
    public List<State> findAll() {
        return statePersistancePort.findAll();
    }

    @Override
    public State save(State state) {
        return statePersistancePort.save(state);
    }

    @Override
    public State update(Long id, State state) {
        return statePersistancePort.findById(id)
                .map(savedState -> {
                    savedState.setName(state.getName());
                    savedState.setIsActive(state.getIsActive());
                    return statePersistancePort.save(savedState);
                }).orElseThrow(StackOverflowError::new);
    }

    @Override
    public void deleteById(Long id) {
        if (statePersistancePort.findById(id).isEmpty()) {
            throw new StateNotFoundException();
        }
        statePersistancePort.deleteById(id);
    }
}
