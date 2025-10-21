package pe.edu.upeu.microservice_schedule.domain.port.in;

import pe.edu.upeu.microservice_schedule.domain.model.WeekDay;

import java.util.List;
import java.util.Optional;

public interface WeekDayUseCase {
    WeekDay create(WeekDay weekDay);
    Optional<WeekDay> findById(Long id);
    List<WeekDay> findAll();
    WeekDay update(Long id, WeekDay weekDay);
    void delete(Long id);
}
