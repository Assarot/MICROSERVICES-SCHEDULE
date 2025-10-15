package pe.edu.upeu.microservice_course_management.application.ports.input;

import pe.edu.upeu.microservice_course_management.domain.model.CourseAssignment;
import pe.edu.upeu.microservice_course_management.domain.model.CourseMode;

import java.util.List;

public interface CourseAssignmentServicePort {

    CourseAssignment findById(Long id);
    List<CourseAssignment> findAll();
    CourseAssignment save(CourseAssignment courseAssignment);
    CourseAssignment update(Long id, CourseAssignment courseAssignment);
    void deleteById(Long id);

}
