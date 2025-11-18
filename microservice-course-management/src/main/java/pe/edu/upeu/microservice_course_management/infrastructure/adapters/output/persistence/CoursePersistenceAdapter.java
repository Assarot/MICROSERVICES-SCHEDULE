package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_course_management.application.ports.output.CoursePersistencePort;
import pe.edu.upeu.microservice_course_management.domain.model.Course;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.entity.CourseEntity;
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
    public Optional<Course> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toCourse);
    }

    @Override
    public List<Course> findAll() {
        return mapper.toCourseList(repository.findAll());
    }

    @Override
    public Course save(Course course) {
        if (course.getIdCourse() == null) {
            CourseEntity entity = mapper.toCourseEntity(course);
            return mapper.toCourse(repository.save(entity));
        }
        CourseEntity entity = repository.findById(course.getIdCourse())
                .orElseThrow();
        entity.setName(course.getName());
        entity.setDescription(course.getDescription());
        entity.setCode(course.getCode());
        entity.setDuration(course.getDuration());
        entity.setPracticalHours(course.getPracticalHours());
        entity.setTheoreticalHours(course.getTheoreticalHours());
        entity.setTotalHours(course.getTotalHours());
        entity.setGroup(mapper.mapGroupToEntity(course.getGroup()));
        entity.setCourseType(mapper.mapCourseTypeToEntity(course.getCourseType()));
        entity.setPlan(mapper.mapPlanToEntity(course.getPlan()));
        return mapper.toCourse(repository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
