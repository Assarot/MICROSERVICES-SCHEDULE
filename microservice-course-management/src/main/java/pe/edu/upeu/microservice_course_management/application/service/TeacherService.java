package pe.edu.upeu.microservice_course_management.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upeu.microservice_course_management.application.ports.input.TeacherServicePort;
import pe.edu.upeu.microservice_course_management.application.ports.output.TeacherPersistencePort;
import pe.edu.upeu.microservice_course_management.domain.exception.TeacherNotFoundException;
import pe.edu.upeu.microservice_course_management.domain.model.Teacher;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService implements TeacherServicePort {

    private final TeacherPersistencePort persistencePort;

    @Override
    public Teacher findById(Long id) {
        return persistencePort.findById(id)
                .orElseThrow(TeacherNotFoundException::new);
    }

    @Override
    public List<Teacher> findAll() {
        return persistencePort.findAll();
    }

    @Override
    public Teacher save(Teacher teacher) {
        return persistencePort.save(teacher);
    }

    @Override
    public Teacher update(Long id, Teacher teacher) {
        return persistencePort.findById(id)
                .map(saveTeacher -> {
                    saveTeacher.setName(teacher.getName());
                    saveTeacher.setLastName(teacher.getLastName());
                    saveTeacher.setEmail(teacher.getEmail());
                    return persistencePort.save(saveTeacher);
                }).orElseThrow(TeacherNotFoundException::new);
    }

    @Override
    public void deleteById(Long id) {
        if (persistencePort.findById(id).isEmpty()) {
            throw new TeacherNotFoundException();
        }
        persistencePort.deleteById(id);
    }
}
