package pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_schedule.domain.model.Schedule;
import pe.edu.upeu.microservice_schedule.domain.port.out.ScheduleRepositoryPort;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.mapper.ScheduleMapper;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.repository.ScheduleJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ScheduleRepositoryAdapter implements ScheduleRepositoryPort {
    
    private final ScheduleJpaRepository jpaRepository;
    private final ScheduleMapper mapper;

    @Override
    public Schedule save(Schedule schedule) {
        var entity = mapper.toEntity(schedule);
        var savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Schedule> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Schedule> findAll() {
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
