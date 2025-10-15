package pe.edu.upeu.microservice_course_management.application.ports.input;

import pe.edu.upeu.microservice_course_management.domain.model.Cycle;

import java.util.List;

public interface CycleServicePort {

    Cycle findById(Long id);
    List<Cycle> findAll();
    Cycle save(Cycle cycle);
    Cycle update(Long id, Cycle cycle);
    void deleteById(Long id);

}
