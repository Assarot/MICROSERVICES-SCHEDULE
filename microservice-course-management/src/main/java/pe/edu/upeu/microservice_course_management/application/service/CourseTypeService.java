package pe.edu.upeu.microservice_course_management.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upeu.microservice_course_management.application.ports.input.CourseTypeServicePort;
import pe.edu.upeu.microservice_course_management.application.ports.output.CourseTypePersistencePort;
import pe.edu.upeu.microservice_course_management.domain.exception.CourseTypeNotFoundException;
import pe.edu.upeu.microservice_course_management.domain.model.CourseType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseTypeService implements CourseTypeServicePort {

    private final CourseTypePersistencePort persistencePort;

    @Override
    public CourseType findById(Long id) {
        return persistencePort.findById(id)
                .orElseThrow(CourseTypeNotFoundException::new);
    }

    @Override
    public List<CourseType> findAll() {
        return persistencePort.findAll();
    }

    @Override
    public CourseType save(CourseType courseType) {
        return persistencePort.save(courseType);
    }

    @Override
    public CourseType update(Long id, CourseType courseType) {
        return persistencePort.findById(id)
                .map(saveCourseType -> {
                    saveCourseType.setName(courseType.getName());
                    return persistencePort.save(saveCourseType);
                }).orElseThrow(CourseTypeNotFoundException::new);
    }

    @Override
    public void deleteById(Long id) {
        if (persistencePort.findById(id).isEmpty()) {
            throw new CourseTypeNotFoundException();
        }

        persistencePort.deleteById(id);
    }
}
