package pe.edu.upeu.microservice_course_management.application.ports.input;

import pe.edu.upeu.microservice_course_management.domain.model.CourseMode;

import java.util.List;

public interface CourseModeServicePort {
    CourseMode findById(Long id);
    List<CourseMode> findAll();
    CourseMode save(CourseMode courseMode);
    CourseMode update(Long id, CourseMode courseMode);
    void deleteById(Long id);
}
