package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_course_management.application.ports.output.CourseModePersistencePort;
import pe.edu.upeu.microservice_course_management.domain.model.CourseMode;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.mapper.CourseModePersistenceMapper;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.repository.CourseModeRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CourseModePersistenceAdapter implements CourseModePersistencePort {

    private final CourseModeRepository repository;
    private final CourseModePersistenceMapper mapper;


    @Override
    public Optional<CourseMode> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toCourseMode);
    }

    @Override
    public List<CourseMode> findAll() {
        return mapper.toCourseModeList(repository.findAll());
    }

    @Override
    public CourseMode save(CourseMode courseMode) {
        return mapper.toCourseMode(repository.save(mapper.toCourseModeEntity(courseMode)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
