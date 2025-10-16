package pe.edu.upeu.microservice_schedule.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.microservice_schedule.domain.model.ScheduleAssignment;
import pe.edu.upeu.microservice_schedule.domain.port.in.ScheduleAssignmentUseCase;
import pe.edu.upeu.microservice_schedule.domain.port.out.ScheduleAssignmentRepositoryPort;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleAssignmentService implements ScheduleAssignmentUseCase {
    
    private final ScheduleAssignmentRepositoryPort repositoryPort;

    @Override
    public ScheduleAssignment create(ScheduleAssignment scheduleAssignment) {
        return repositoryPort.save(scheduleAssignment);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ScheduleAssignment> findById(Long id) {
        return repositoryPort.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleAssignment> findAll() {
        return repositoryPort.findAll();
    }

    @Override
    public ScheduleAssignment update(Long id, ScheduleAssignment scheduleAssignment) {
        if (!repositoryPort.existsById(id)) {
            throw new RuntimeException("ScheduleAssignment not found with id: " + id);
        }
        scheduleAssignment.setIdScheduleAssignment(id);
        return repositoryPort.save(scheduleAssignment);
    }

    @Override
    public void delete(Long id) {
        if (!repositoryPort.existsById(id)) {
            throw new RuntimeException("ScheduleAssignment not found with id: " + id);
        }
        repositoryPort.deleteById(id);
    }
}
