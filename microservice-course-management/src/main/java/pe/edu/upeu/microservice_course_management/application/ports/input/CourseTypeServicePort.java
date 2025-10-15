package pe.edu.upeu.microservice_course_management.application.ports.input;

import pe.edu.upeu.microservice_course_management.domain.model.CourseType;

import java.util.List;

public interface CourseTypeServicePort {

    CourseType findById(Long id);
    List<CourseType> findAll();
    CourseType save(CourseType courseType);
    CourseType update(Long id, CourseType courseType);
    void deleteById(Long id);

}
