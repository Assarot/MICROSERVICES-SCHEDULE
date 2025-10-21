package pe.edu.upeu.microservice_schedule.domain.port.in;

import pe.edu.upeu.microservice_schedule.domain.model.ScheduleAssignment;

import java.util.List;
import java.util.Optional;

public interface ScheduleAssignmentUseCase {
    ScheduleAssignment create(ScheduleAssignment scheduleAssignment);
    Optional<ScheduleAssignment> findById(Long id);
    List<ScheduleAssignment> findAll();
    ScheduleAssignment update(Long id, ScheduleAssignment scheduleAssignment);
    void delete(Long id);
}
