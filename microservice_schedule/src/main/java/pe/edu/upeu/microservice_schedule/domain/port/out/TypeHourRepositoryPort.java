package pe.edu.upeu.microservice_schedule.domain.port.out;

import pe.edu.upeu.microservice_schedule.domain.model.TypeHour;

import java.util.List;
import java.util.Optional;

public interface TypeHourRepositoryPort {
    TypeHour save(TypeHour typeHour);
    Optional<TypeHour> findById(Long id);
    List<TypeHour> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
}
