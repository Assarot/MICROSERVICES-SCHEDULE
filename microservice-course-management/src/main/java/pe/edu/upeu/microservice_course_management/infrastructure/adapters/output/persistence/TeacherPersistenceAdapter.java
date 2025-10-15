package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_course_management.application.ports.output.TeacherPersistencePort;
import pe.edu.upeu.microservice_course_management.domain.model.Teacher;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.mapper.TeacherPersistenceMapper;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.repository.TeacherRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TeacherPersistenceAdapter implements TeacherPersistencePort {

    private final TeacherRepository repository;
    private final TeacherPersistenceMapper mapper;

    @Override
    public Optional<Teacher> findById(long id) {
        return repository.findById(id)
                .map(mapper::toTeacher);
    }

    @Override
    public List<Teacher> findAll() {
        return mapper.toTeacherList(repository.findAll());
    }

    @Override
    public Teacher save(Teacher teacher) {
        return mapper.toTeacher(repository.save(mapper.toTeacherEntity(teacher)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
