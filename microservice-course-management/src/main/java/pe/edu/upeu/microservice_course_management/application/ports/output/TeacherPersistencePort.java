package pe.edu.upeu.microservice_course_management.application.ports.output;

import pe.edu.upeu.microservice_course_management.domain.model.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherPersistencePort {
    Optional<Teacher> findById(long id);
    List<Teacher> findAll();
    Teacher save(Teacher teacher);
    void deleteById(Long id);
}
