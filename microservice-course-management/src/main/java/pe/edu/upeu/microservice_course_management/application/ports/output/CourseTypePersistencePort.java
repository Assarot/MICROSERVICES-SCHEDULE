package pe.edu.upeu.microservice_course_management.application.ports.output;

import pe.edu.upeu.microservice_course_management.domain.model.CourseType;

import java.util.List;
import java.util.Optional;

public interface CourseTypePersistencePort {
    Optional<CourseType> findById(long id);
    List<CourseType> findAll();
    CourseType save(CourseType courseType);
    void deleteById(Long id);
}
