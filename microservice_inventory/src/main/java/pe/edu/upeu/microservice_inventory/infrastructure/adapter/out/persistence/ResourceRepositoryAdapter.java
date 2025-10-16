package pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_inventory.domain.model.Resource;
import pe.edu.upeu.microservice_inventory.domain.port.out.ResourceRepositoryPort;
import pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence.mapper.PersistenceMapper;
import pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence.repository.ResourceJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ResourceRepositoryAdapter implements ResourceRepositoryPort {
    
    private final ResourceJpaRepository resourceJpaRepository;
    private final PersistenceMapper mapper;

    @Override
    public Resource save(Resource resource) {
        var entity = mapper.toResourceEntity(resource);
        var savedEntity = resourceJpaRepository.save(entity);
        return mapper.toResourceDomain(savedEntity);
    }

    @Override
    public Optional<Resource> findById(Long id) {
        return resourceJpaRepository.findById(id)
                .map(mapper::toResourceDomain);
    }

    @Override
    public List<Resource> findAll() {
        return resourceJpaRepository.findAll().stream()
                .map(mapper::toResourceDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Resource> findByResourceTypeId(Long typeId) {
        return resourceJpaRepository.findByIdResourceType(typeId).stream()
                .map(mapper::toResourceDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Resource> findByStateId(Long stateId) {
        return resourceJpaRepository.findByIdState(stateId).stream()
                .map(mapper::toResourceDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        resourceJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return resourceJpaRepository.existsById(id);
    }
}
