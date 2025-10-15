package pe.edu.upeu.microserviceenviroment.application.ports.output;

import pe.edu.upeu.microserviceenviroment.domain.model.State;

import java.util.List;
import java.util.Optional;

public interface StatePersistancePort {
    Optional<State> findById(Long id);
    List<State> findAll();
    State save(State state);
    void deleteById(Long id);
}
