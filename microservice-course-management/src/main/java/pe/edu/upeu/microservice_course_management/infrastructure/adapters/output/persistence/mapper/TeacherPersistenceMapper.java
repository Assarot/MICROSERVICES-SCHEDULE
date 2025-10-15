package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.mapper;

import org.mapstruct.Mapper;
import pe.edu.upeu.microservice_course_management.domain.model.Teacher;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.entity.TeacherEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TeacherPersistenceMapper {
    TeacherEntity toTeacherEntity(Teacher teacher);
    Teacher toTeacher(TeacherEntity entity);
    List<Teacher> toTeacherList(List<TeacherEntity> entityList);
}
