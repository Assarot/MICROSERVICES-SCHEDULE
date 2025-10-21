package pe.edu.upeu.microservice_inventory.domain.port.in;

import pe.edu.upeu.microservice_inventory.domain.model.State;

import java.util.List;
import java.util.Optional;

public interface StateUseCase {
    State createState(State state);
    State updateState(Long id, State state);
    void deleteState(Long id);
    Optional<State> getStateById(Long id);
    List<State> getAllStates();
    List<State> getActiveStates();
}
