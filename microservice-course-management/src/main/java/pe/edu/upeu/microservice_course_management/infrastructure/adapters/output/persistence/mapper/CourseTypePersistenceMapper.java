package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.mapper;

import org.mapstruct.Mapper;
import pe.edu.upeu.microservice_course_management.domain.model.CourseType;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.entity.CourseTypeEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseTypePersistenceMapper {

    CourseTypeEntity toCourseTypeEntity(CourseType courseType);
    CourseType toCourseType(CourseTypeEntity entity);
    List<CourseType> toCourseTypeList(List<CourseTypeEntity> entityList);

}
