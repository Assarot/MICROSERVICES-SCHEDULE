package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_course_management.application.ports.output.CourseTypePersistencePort;
import pe.edu.upeu.microservice_course_management.domain.model.CourseType;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.entity.CourseTypeEntity;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.mapper.CourseTypePersistenceMapper;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.repository.CourseTypeRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CourseTypePersistenceAdapter implements CourseTypePersistencePort {

    private final CourseTypeRepository repository;
    private final CourseTypePersistenceMapper mapper;

    @Override
    public Optional<CourseType> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toCourseType);
    }

    @Override
    public List<CourseType> findAll() {
        return mapper.toCourseTypeList(repository.findAll());
    }

    @Override
    public CourseType save(CourseType courseType) {
        if (courseType.getIdCourseType() == null) {
            CourseTypeEntity entity = mapper.toCourseTypeEntity(courseType);
            return mapper.toCourseType(repository.save(entity));
        };
        CourseTypeEntity entity = repository.findById(courseType.getIdCourseType())
                .orElseThrow();
        entity.setName(courseType.getName());
        return mapper.toCourseType(repository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
