package pe.edu.upeu.microservice_course_management.application.ports.input;

import pe.edu.upeu.microservice_course_management.domain.model.Teacher;

import java.util.List;

public interface TeacherServicePort {
    Teacher findById(Long id);
    List<Teacher> findAll();
    Teacher save(Teacher teacher);
    Teacher update(Long id, Teacher teacher);
    void deleteById(Long id);
}
