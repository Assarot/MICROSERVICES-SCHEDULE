package pe.edu.upeu.microservice_course_management.application.ports.output;

import pe.edu.upeu.microservice_course_management.domain.model.Cycle;

import java.util.List;
import java.util.Optional;

public interface CyclePersistencePort {

    Optional<Cycle> finById(Long id);
    List<Cycle> finAll();
    Cycle save(Cycle cycle);
    void deleteById(Long id);

}
