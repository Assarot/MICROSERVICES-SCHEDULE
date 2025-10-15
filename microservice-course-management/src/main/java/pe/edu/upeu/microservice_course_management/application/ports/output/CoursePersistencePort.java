package pe.edu.upeu.microservice_course_management.application.ports.output;

import pe.edu.upeu.microservice_course_management.domain.model.Course;

import java.util.List;
import java.util.Optional;

public interface CoursePersistencePort {
    Optional<Course> findById(long id);
    List<Course> findAll();
    Course save(Course course);
    void deleteById(Long id);
}
