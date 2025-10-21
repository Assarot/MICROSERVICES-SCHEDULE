package pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_schedule.domain.model.TypeHour;
import pe.edu.upeu.microservice_schedule.domain.port.out.TypeHourRepositoryPort;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.mapper.TypeHourMapper;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.repository.TypeHourJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TypeHourRepositoryAdapter implements TypeHourRepositoryPort {
    
    private final TypeHourJpaRepository jpaRepository;
    private final TypeHourMapper mapper;

    @Override
    public TypeHour save(TypeHour typeHour) {
        var entity = mapper.toEntity(typeHour);
        var savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<TypeHour> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<TypeHour> findAll() {
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
