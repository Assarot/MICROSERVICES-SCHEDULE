package pe.edu.upeu.microservice_schedule.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.microservice_schedule.domain.model.Schedule;
import pe.edu.upeu.microservice_schedule.domain.port.in.ScheduleUseCase;
import pe.edu.upeu.microservice_schedule.domain.port.out.ScheduleRepositoryPort;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService implements ScheduleUseCase {
    
    private final ScheduleRepositoryPort repositoryPort;

    @Override
    public Schedule create(Schedule schedule) {
        return repositoryPort.save(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Schedule> findById(Long id) {
        return repositoryPort.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Schedule> findAll() {
        return repositoryPort.findAll();
    }

    @Override
    public Schedule update(Long id, Schedule schedule) {
        if (!repositoryPort.existsById(id)) {
            throw new RuntimeException("Schedule not found with id: " + id);
        }
        schedule.setIdSchedule(id);
        return repositoryPort.save(schedule);
    }

    @Override
    public void delete(Long id) {
        if (!repositoryPort.existsById(id)) {
            throw new RuntimeException("Schedule not found with id: " + id);
        }
        repositoryPort.deleteById(id);
    }
}
