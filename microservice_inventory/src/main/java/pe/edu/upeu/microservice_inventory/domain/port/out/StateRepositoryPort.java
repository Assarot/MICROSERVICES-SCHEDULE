package pe.edu.upeu.microservice_inventory.domain.port.out;

import pe.edu.upeu.microservice_inventory.domain.model.State;

import java.util.List;
import java.util.Optional;

public interface StateRepositoryPort {
    State save(State state);
    Optional<State> findById(Long id);
    List<State> findAll();
    List<State> findByIsActive(Boolean isActive);
    void deleteById(Long id);
    boolean existsById(Long id);
}
