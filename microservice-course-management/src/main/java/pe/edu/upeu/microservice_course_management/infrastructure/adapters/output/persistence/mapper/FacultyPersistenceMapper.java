package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.mapper;

import org.mapstruct.Mapper;
import pe.edu.upeu.microservice_course_management.domain.model.Faculty;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.entity.FacultyEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FacultyPersistenceMapper {
    FacultyEntity toFacultyEntity(Faculty faculty);
    Faculty toFaculty(FacultyEntity entity);
    List<Faculty> toFacultyList(List<FacultyEntity> entityList);
}
