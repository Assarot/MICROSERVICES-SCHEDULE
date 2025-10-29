package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microserviceinventory2.application.ports.output.StatePersistencePort;
import pe.edu.upeu.microserviceinventory2.domain.model.State;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.StateEntity;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.mapper.StatePersistenceMapper;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.repository.StateRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StatePersistenceAdapter implements StatePersistencePort {

    private final StateRepository repository;
    private final StatePersistenceMapper mapper;

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
        StateEntity entity = mapper.toStateEntity(state);
        StateEntity saved = repository.save(entity);
        return mapper.toState(saved);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
