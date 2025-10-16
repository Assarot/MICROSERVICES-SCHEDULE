package pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_schedule.domain.model.ScheduleAssignment;
import pe.edu.upeu.microservice_schedule.domain.port.out.ScheduleAssignmentRepositoryPort;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.mapper.ScheduleAssignmentMapper;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.repository.ScheduleAssignmentJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ScheduleAssignmentRepositoryAdapter implements ScheduleAssignmentRepositoryPort {
    
    private final ScheduleAssignmentJpaRepository jpaRepository;
    private final ScheduleAssignmentMapper mapper;

    @Override
    public ScheduleAssignment save(ScheduleAssignment scheduleAssignment) {
        var entity = mapper.toEntity(scheduleAssignment);
        var savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<ScheduleAssignment> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<ScheduleAssignment> findAll() {
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
