package pe.edu.upeu.microservice_course_management.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upeu.microservice_course_management.application.ports.input.FacultyServicePort;
import pe.edu.upeu.microservice_course_management.application.ports.output.FacultyPersistencePort;
import pe.edu.upeu.microservice_course_management.domain.exception.FacultyNotFoudException;
import pe.edu.upeu.microservice_course_management.domain.model.Faculty;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacultyService implements FacultyServicePort {
    private final FacultyPersistencePort persistencePort;

    @Override
    public Faculty findById(Long id) {
        return persistencePort.findById(id)
                .orElseThrow(FacultyNotFoudException::new);
    }

    @Override
    public List<Faculty> findAll() {
        return persistencePort.findAll();
    }

    @Override
    public Faculty save(Faculty faculty) {
        return persistencePort.save(faculty);
    }

    @Override
    public Faculty update(Long id, Faculty faculty) {
        return persistencePort.findById(id)
                .map(saveFaculty -> {
                    saveFaculty.setName(faculty.getName());
                    return persistencePort.save(saveFaculty);
                }).orElseThrow(FacultyNotFoudException::new);
    }

    @Override
    public void deleteById(Long id) {
        if (persistencePort.findById(id).isEmpty()) {
            throw new FacultyNotFoudException();
        }
        persistencePort.deleteById(id);
    }
}
