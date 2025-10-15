package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.mapper;

import org.mapstruct.Mapper;
import pe.edu.upeu.microservice_course_management.domain.model.CourseMode;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.entity.CourseModeEntity;

import java.util.List;

@Mapper(componentModel = "spring", uses = {GroupPersistenceMapper.class})
public interface CourseModePersistenceMapper {

    CourseModeEntity toCourseModeEntity(CourseMode courseMode);
    CourseMode toCourseMode(CourseModeEntity entity);
    List<CourseMode> toCourseModeList(List<CourseModeEntity> entityList);

}
