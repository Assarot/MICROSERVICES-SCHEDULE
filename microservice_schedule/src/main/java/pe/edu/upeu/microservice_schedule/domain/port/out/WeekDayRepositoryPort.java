package pe.edu.upeu.microservice_schedule.domain.port.out;

import pe.edu.upeu.microservice_schedule.domain.model.WeekDay;

import java.util.List;
import java.util.Optional;

public interface WeekDayRepositoryPort {
    WeekDay save(WeekDay weekDay);
    Optional<WeekDay> findById(Long id);
    List<WeekDay> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
}
