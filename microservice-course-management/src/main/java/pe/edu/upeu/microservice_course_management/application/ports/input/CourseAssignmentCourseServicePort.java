package pe.edu.upeu.microservice_course_management.application.ports.input;

import pe.edu.upeu.microservice_course_management.domain.model.CourseAssignmentCourse;

import java.util.List;

public interface CourseAssignmentCourseServicePort {
    CourseAssignmentCourse findById(Long id);
    List<CourseAssignmentCourse> findAll();
    CourseAssignmentCourse save(CourseAssignmentCourse courseAssignmentCourse);
    CourseAssignmentCourse update(Long id, CourseAssignmentCourse courseAssignmentCourse);
    void deleteById(Long id);
}
