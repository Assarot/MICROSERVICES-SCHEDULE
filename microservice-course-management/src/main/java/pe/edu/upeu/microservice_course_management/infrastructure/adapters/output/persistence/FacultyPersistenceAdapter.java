package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_course_management.application.ports.output.FacultyPersistencePort;
import pe.edu.upeu.microservice_course_management.domain.model.Faculty;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.mapper.FacultyPersistenceMapper;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.repository.FacultyRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FacultyPersistenceAdapter implements FacultyPersistencePort {

    private final FacultyRepository repository;
    private final FacultyPersistenceMapper mapper;

    @Override
    public Optional<Faculty> findById(long id) {
        return repository.findById(id)
                .map(mapper::toFaculty);
    }

    @Override
    public List<Faculty> findAll() {
        return mapper.toFacultyList(repository.findAll());
    }

    @Override
    public Faculty save(Faculty faculty) {
        return mapper.toFaculty(repository.save(mapper.toFacultyEntity(faculty)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
