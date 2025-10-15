package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_course_management.application.ports.output.CourseAssignmentPersistencePort;
import pe.edu.upeu.microservice_course_management.domain.model.CourseAssignment;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.mapper.CourseAssignmentPersistenceMapper;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.mapper.CourseModePersistenceMapper;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.repository.CourseAssignmentRepository;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.repository.CourseModeRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CourseAssignmentPersistenceAdapter implements CourseAssignmentPersistencePort {

    private final CourseAssignmentRepository repository;
    private final CourseAssignmentPersistenceMapper mapper;

    @Override
    public Optional<CourseAssignment> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toCourseAssignment);
    }

    @Override
    public List<CourseAssignment> findAll() {
        return mapper.toCourseAssignmentList(repository.findAll());
    }

    @Override
    public CourseAssignment save(CourseAssignment courseAssignment) {
        return mapper.toCourseAssignment(repository.save(mapper.toCourseAssignmentEntity(courseAssignment)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
