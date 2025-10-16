package pe.edu.upeu.microservice_schedule.domain.port.out;

import pe.edu.upeu.microservice_schedule.domain.model.ScheduleAssignment;

import java.util.List;
import java.util.Optional;

public interface ScheduleAssignmentRepositoryPort {
    ScheduleAssignment save(ScheduleAssignment scheduleAssignment);
    Optional<ScheduleAssignment> findById(Long id);
    List<ScheduleAssignment> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
}
