package pe.edu.upeu.microservice_course_management.application.ports.output;

import pe.edu.upeu.microservice_course_management.domain.model.CourseAssignmentCourse;

import java.util.List;
import java.util.Optional;

public interface CourseAssignmentCoursePersistencePort {
    Optional<CourseAssignmentCourse> findById(Long id);
    List<CourseAssignmentCourse> findAll();
    CourseAssignmentCourse save(CourseAssignmentCourse courseAssignmentCourse);
    void deleteById(Long id);
}
