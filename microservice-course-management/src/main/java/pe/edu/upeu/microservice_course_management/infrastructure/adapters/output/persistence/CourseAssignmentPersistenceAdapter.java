package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_course_management.application.ports.output.CourseAssignmentPersistencePort;
import pe.edu.upeu.microservice_course_management.domain.model.CourseAssignment;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.entity.CourseAssignmentEntity;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.mapper.CourseAssignmentPersistenceMapper;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.repository.CourseAssignmentRepository;

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
        if (courseAssignment.getIdCourseAssignment() == null) {
            CourseAssignmentEntity entity = mapper.toCourseAssignmentEntity(courseAssignment);
            return mapper.toCourseAssignment(repository.save(entity));
        }

        // Update
        CourseAssignmentEntity entity = repository.findById(courseAssignment.getIdCourseAssignment())
                .orElseThrow(); // o tu excepción

        // Solo actualizas lo que cambió (teacher)
        entity.setTeacher(mapper.mapTeacherToEntity(courseAssignment.getTeacher()));

        // NO tocas courseAssignmentCourse: Hibernate mantiene su colección
        return mapper.toCourseAssignment(repository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
