package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microserviceenviroment.application.ports.output.StatePersistancePort;
import pe.edu.upeu.microserviceenviroment.domain.model.State;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.mapper.StatePersistenceMapper;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.repository.StateRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StatePersistenceAdapter implements StatePersistancePort {

    private final StatePersistenceMapper mapper;
    private final StateRepository repository;


    @Override
    public Optional<State> findById(Long id) {
        return repository.findById(id).map(mapper::toState);
    }

    @Override
    public List<State> findAll() {
        return mapper.toStateList(repository.findAll());
    }

    @Override
    public State save(State state) {
        return mapper.toState(repository.save(mapper.toStateEntity(state)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
