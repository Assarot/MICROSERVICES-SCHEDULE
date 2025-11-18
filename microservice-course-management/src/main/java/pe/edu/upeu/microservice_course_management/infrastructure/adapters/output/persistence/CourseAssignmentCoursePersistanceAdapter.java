package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_course_management.application.ports.output.CourseAssignmentCoursePersistencePort;
import pe.edu.upeu.microservice_course_management.domain.model.CourseAssignmentCourse;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.entity.CourseAssignmentCourseEntity;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.mapper.CourseAssignmentCoursePersistanceMapper;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.repository.CourseAssignmentCourseRepository;

import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class CourseAssignmentCoursePersistanceAdapter implements CourseAssignmentCoursePersistencePort {

    private final CourseAssignmentCourseRepository repository;
    private final CourseAssignmentCoursePersistanceMapper mapper;

    @Override
    public Optional<CourseAssignmentCourse> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toCourseAssignmentCourse);
    }

    @Override
    public List<CourseAssignmentCourse> findAll() {
        return mapper.toCourseAssignmentCourseList(repository.findAll());
    }

    @Override
    public CourseAssignmentCourse save(CourseAssignmentCourse courseAssignmentCourse) {
        if (courseAssignmentCourse.getIdCourseAssignmentCourse() == null) {
            CourseAssignmentCourseEntity entity = mapper.toCourseAssignmentCourseToEntity(courseAssignmentCourse);
            return mapper.toCourseAssignmentCourse(repository.save(entity));
        }
        CourseAssignmentCourseEntity entity = repository.findById(courseAssignmentCourse.getIdCourseAssignmentCourse())
                .orElseThrow();
        entity.setCourseAssignment(mapper.mapCourseAssignmentToEntity(courseAssignmentCourse.getCourseAssignment()));
        entity.setCourse(mapper.mapCourseToEntity(courseAssignmentCourse.getCourse()));
        return mapper.toCourseAssignmentCourse(repository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
