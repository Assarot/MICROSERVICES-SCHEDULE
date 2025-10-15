package pe.edu.upeu.microservice_course_management.application.ports.output;

import pe.edu.upeu.microservice_course_management.domain.model.CourseMode;

import java.util.List;
import java.util.Optional;

public interface CourseModePersistencePort {
    Optional<CourseMode> findById(Long id);
    List<CourseMode> findAll();
    CourseMode save(CourseMode courseMode);
    void deleteById(Long id);
}
