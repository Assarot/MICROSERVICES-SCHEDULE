package pe.edu.upeu.microserviceinventory2.application.ports.input;

import pe.edu.upeu.microserviceinventory2.domain.model.State;

import java.util.List;

public interface StateServicePort {
    State findById(Long id);
    List<State> findAll();
    State save(State state);
    State update(Long id, State state);
    void deleteById(Long id);
}
