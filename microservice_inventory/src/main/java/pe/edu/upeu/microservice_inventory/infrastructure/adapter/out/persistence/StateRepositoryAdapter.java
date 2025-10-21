package pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_inventory.domain.model.State;
import pe.edu.upeu.microservice_inventory.domain.port.out.StateRepositoryPort;
import pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence.mapper.PersistenceMapper;
import pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence.repository.StateJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StateRepositoryAdapter implements StateRepositoryPort {
    
    private final StateJpaRepository stateJpaRepository;
    private final PersistenceMapper mapper;

    @Override
    public State save(State state) {
        var entity = mapper.toStateEntity(state);
        var savedEntity = stateJpaRepository.save(entity);
        return mapper.toStateDomain(savedEntity);
    }

    @Override
    public Optional<State> findById(Long id) {
        return stateJpaRepository.findById(id)
                .map(mapper::toStateDomain);
    }

    @Override
    public List<State> findAll() {
        return stateJpaRepository.findAll().stream()
                .map(mapper::toStateDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<State> findByIsActive(Boolean isActive) {
        return stateJpaRepository.findByIsActive(isActive).stream()
                .map(mapper::toStateDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        stateJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return stateJpaRepository.existsById(id);
    }
}
