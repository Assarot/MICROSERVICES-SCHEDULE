package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_course_management.application.ports.output.CoursePersistencePort;
import pe.edu.upeu.microservice_course_management.domain.model.Course;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.mapper.CoursePersistenceMapper;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.repository.CourseRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CoursePersistenceAdapter implements CoursePersistencePort {

    private final CourseRepository repository;
    private final CoursePersistenceMapper mapper;

    @Override
    public Optional<Course> findById(long id) {
        return repository.findById(id)
                .map(mapper::toCourse);
    }

    @Override
    public List<Course> findAll() {
        return mapper.toCourseList(repository.findAll());
    }

    @Override
    public Course save(Course course) {
        return mapper.toCourse(repository.save(mapper.toCourseEntity(course)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
