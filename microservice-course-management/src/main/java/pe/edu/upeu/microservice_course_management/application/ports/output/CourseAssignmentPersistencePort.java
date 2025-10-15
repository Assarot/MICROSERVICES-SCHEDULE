package pe.edu.upeu.microservice_course_management.application.ports.output;

import pe.edu.upeu.microservice_course_management.domain.model.CourseAssignment;

import java.util.List;
import java.util.Optional;

public interface CourseAssignmentPersistencePort {
    Optional<CourseAssignment> findById(Long id);
    List<CourseAssignment> findAll();
    CourseAssignment save(CourseAssignment courseAssignment);
    void deleteById(Long id);
}
