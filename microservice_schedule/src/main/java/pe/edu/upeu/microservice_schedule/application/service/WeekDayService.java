package pe.edu.upeu.microservice_schedule.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.microservice_schedule.domain.model.WeekDay;
import pe.edu.upeu.microservice_schedule.domain.port.in.WeekDayUseCase;
import pe.edu.upeu.microservice_schedule.domain.port.out.WeekDayRepositoryPort;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class WeekDayService implements WeekDayUseCase {
    
    private final WeekDayRepositoryPort repositoryPort;

    @Override
    public WeekDay create(WeekDay weekDay) {
        return repositoryPort.save(weekDay);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WeekDay> findById(Long id) {
        return repositoryPort.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WeekDay> findAll() {
        return repositoryPort.findAll();
    }

    @Override
    public WeekDay update(Long id, WeekDay weekDay) {
        if (!repositoryPort.existsById(id)) {
            throw new RuntimeException("WeekDay not found with id: " + id);
        }
        weekDay.setIdSchedule(id);
        return repositoryPort.save(weekDay);
    }

    @Override
    public void delete(Long id) {
        if (!repositoryPort.existsById(id)) {
            throw new RuntimeException("WeekDay not found with id: " + id);
        }
        repositoryPort.deleteById(id);
    }
}
