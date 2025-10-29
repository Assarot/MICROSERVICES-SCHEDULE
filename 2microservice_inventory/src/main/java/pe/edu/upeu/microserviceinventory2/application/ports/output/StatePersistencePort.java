package pe.edu.upeu.microserviceinventory2.application.ports.output;

import pe.edu.upeu.microserviceinventory2.domain.model.State;

import java.util.List;
import java.util.Optional;

public interface StatePersistencePort {
    Optional<State> findById(Long id);
    List<State> findAll();
    State save(State state);
    void deleteById(Long id);
}
