package pe.edu.upeu.microservice_course_management.application.ports.input;

import pe.edu.upeu.microservice_course_management.domain.model.Course;

import java.util.List;

public interface CourseServicePort {
    Course findById(Long id);
    List<Course> findAll();
    Course save(Course course);
    Course update(Long id, Course course);
    void deleteById(Long id);
}
