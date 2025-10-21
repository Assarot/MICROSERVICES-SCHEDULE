package pe.edu.upeu.microservice_schedule.domain.port.in;

import pe.edu.upeu.microservice_schedule.domain.model.TypeHour;

import java.util.List;
import java.util.Optional;

public interface TypeHourUseCase {
    TypeHour create(TypeHour typeHour);
    Optional<TypeHour> findById(Long id);
    List<TypeHour> findAll();
    TypeHour update(Long id, TypeHour typeHour);
    void delete(Long id);
}
