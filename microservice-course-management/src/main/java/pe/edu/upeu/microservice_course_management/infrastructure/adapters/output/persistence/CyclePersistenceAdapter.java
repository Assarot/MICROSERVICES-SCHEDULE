package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_course_management.application.ports.output.CyclePersistencePort;
import pe.edu.upeu.microservice_course_management.domain.model.Cycle;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.mapper.CyclePersistenceMapper;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.repository.CycleRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CyclePersistenceAdapter implements CyclePersistencePort {

    private final CycleRepository repository;
    private final CyclePersistenceMapper mapper;

    @Override
    public Optional<Cycle> finById(Long id) {
        return repository.findById(id)
                .map(mapper::toCycle);
    }

    @Override
    public List<Cycle> finAll() {
        return mapper.toCycleList(repository.findAll());
    }

    @Override
    public Cycle save(Cycle cycle) {
        var entity = mapper.toCycleEntity(cycle);
        var savedEntity = repository.save(entity);
        return mapper.toCycle(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
