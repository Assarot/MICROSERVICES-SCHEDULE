package pe.edu.upeu.microservice_schedule.domain.port.in;

import pe.edu.upeu.microservice_schedule.domain.model.Schedule;

import java.util.List;
import java.util.Optional;

public interface ScheduleUseCase {
    Schedule create(Schedule schedule);
    Optional<Schedule> findById(Long id);
    List<Schedule> findAll();
    Schedule update(Long id, Schedule schedule);
    void delete(Long id);
}
