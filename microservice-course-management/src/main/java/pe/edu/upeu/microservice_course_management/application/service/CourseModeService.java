package pe.edu.upeu.microservice_course_management.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upeu.microservice_course_management.application.ports.input.CourseModeServicePort;
import pe.edu.upeu.microservice_course_management.application.ports.output.CourseModePersistencePort;
import pe.edu.upeu.microservice_course_management.domain.exception.CourseModeNotFoundException;
import pe.edu.upeu.microservice_course_management.domain.model.CourseMode;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseModeService implements CourseModeServicePort {

    private final CourseModePersistencePort persistencePort;

    @Override
    public CourseMode findById(Long id) {
        return persistencePort.findById(id)
                .orElseThrow(CourseModeNotFoundException::new);
    }

    @Override
    public List<CourseMode> findAll() {
        return persistencePort.findAll();
    }

    @Override
    public CourseMode save(CourseMode courseMode) {
        return persistencePort.save(courseMode);
    }

    @Override
    public CourseMode update(Long id, CourseMode courseMode) {
        return persistencePort.findById(id)
                .map(saveCourseMode -> {
                    saveCourseMode.setName(courseMode.getName());
                    return persistencePort.save(saveCourseMode);
                }).orElseThrow(CourseModeNotFoundException::new);
    }

    @Override
    public void deleteById(Long id) {
        if (persistencePort.findById(id).isEmpty()) {
            throw new CourseModeNotFoundException();
        }
        persistencePort.deleteById(id);
    }
}
