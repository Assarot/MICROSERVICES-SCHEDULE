package pe.edu.upeu.microserviceenviroment.application.ports.input;

import pe.edu.upeu.microserviceenviroment.domain.model.State;

import java.util.List;

public interface StateServicePort {
    State findById(Long id);
    List<State> findAll();
    State save(State state);
    State update(Long id,State state);
    void deleteById(Long id);
}
