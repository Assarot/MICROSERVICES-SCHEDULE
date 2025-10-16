package pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_schedule.domain.model.TypeAssignment;
import pe.edu.upeu.microservice_schedule.domain.port.out.TypeAssignmentRepositoryPort;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.mapper.TypeAssignmentMapper;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.repository.TypeAssignmentJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TypeAssignmentRepositoryAdapter implements TypeAssignmentRepositoryPort {
    
    private final TypeAssignmentJpaRepository jpaRepository;
    private final TypeAssignmentMapper mapper;

    @Override
    public TypeAssignment save(TypeAssignment typeAssignment) {
        var entity = mapper.toEntity(typeAssignment);
        var savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<TypeAssignment> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<TypeAssignment> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
}
