package pe.edu.upeu.microservice_schedule.domain.port.out;

import pe.edu.upeu.microservice_schedule.domain.model.Schedule;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepositoryPort {
    Schedule save(Schedule schedule);
    Optional<Schedule> findById(Long id);
    List<Schedule> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
}
