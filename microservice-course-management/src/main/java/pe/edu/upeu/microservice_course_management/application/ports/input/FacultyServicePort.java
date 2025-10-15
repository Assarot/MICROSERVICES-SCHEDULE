package pe.edu.upeu.microservice_course_management.application.ports.input;

import pe.edu.upeu.microservice_course_management.domain.model.Faculty;

import java.util.List;

public interface FacultyServicePort {
    Faculty findById(Long id);
    List<Faculty> findAll();
    Faculty save(Faculty faculty);
    Faculty update(Long id, Faculty faculty);
    void deleteById(Long id);
}
