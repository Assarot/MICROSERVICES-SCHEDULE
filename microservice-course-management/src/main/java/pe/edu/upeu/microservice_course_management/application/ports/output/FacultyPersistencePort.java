package pe.edu.upeu.microservice_course_management.application.ports.output;

import pe.edu.upeu.microservice_course_management.domain.model.Faculty;

import java.util.List;
import java.util.Optional;

public interface FacultyPersistencePort {
    Optional<Faculty> findById(long id);
    List<Faculty> findAll();
    Faculty save(Faculty faculty);
    void deleteById(Long id);
}
