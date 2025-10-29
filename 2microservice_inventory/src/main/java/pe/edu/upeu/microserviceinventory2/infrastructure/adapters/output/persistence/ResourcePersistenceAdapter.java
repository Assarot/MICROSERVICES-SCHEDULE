package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microserviceinventory2.application.ports.output.ResourcePersistencePort;
import pe.edu.upeu.microserviceinventory2.domain.model.Resource;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.mapper.ResourcePersistenceMapper;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.repository.ResourceRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ResourcePersistenceAdapter implements ResourcePersistencePort {

    private final ResourceRepository repository;
    private final ResourcePersistenceMapper mapper;

    @Override
    public Optional<Resource> findById(Long id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<Resource> findAll() {
        return mapper.toModelList(repository.findAll());
    }

    @Override
    public List<Resource> findByTypeId(Long typeId) {
        return mapper.toModelList(repository.findByIdResourceType(typeId));
    }

    @Override
    public List<Resource> findByStateId(Long stateId) {
        return mapper.toModelList(repository.findByIdState(stateId));
    }

    @Override
    public Resource save(Resource resource) {
        var saved = repository.save(mapper.toEntity(resource));
        return mapper.toModel(saved);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
