package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pe.edu.upeu.microservice_course_management.domain.model.*;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.entity.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CourseAssignmentPersistenceMapper.class,
        TeacherPersistenceMapper.class})
public interface CourseAssignmentPersistenceMapper {
    @Mapping(target = "course", source = "course", qualifiedByName = "mapCourseToEntity")
    @Mapping(target = "teacher", source = "teacher", qualifiedByName = "mapTeacherToEntity")
    CourseAssignmentEntity toCourseAssignmentEntity(CourseAssignment courseAssignment);

    @Mapping(target = "course", source = "course", qualifiedByName = "mapCourseToDomain")
    @Mapping(target = "teacher", source = "teacher", qualifiedByName = "mapTeacherToDomain")
    CourseAssignment toCourseAssignment(CourseAssignmentEntity entity);
    List<CourseAssignment> toCourseAssignmentList(List<CourseAssignmentEntity> entityList);

    // Course

    @Named("mapCourseToEntity")
    default CourseEntity mapCourseToEntity(Course course) {
        if (course == null) return null;
        CourseEntity ce = new CourseEntity();
        ce.setIdCourse(course.getIdCourse());
        ce.setName(course.getName());
        return ce;
    }

    @Named("mapCourseToDomain")
    default Course mapCourseToDomain(CourseEntity entity) {
        if (entity == null) return null;
        Course c = new Course();
        c.setIdCourse(entity.getIdCourse());
        c.setName(entity.getName());
        return c;
    }

    // Teacher

    @Named("mapTeacherToEntity")
    default TeacherEntity mapTeacherToEntity(Teacher teacher) {
        if (teacher == null) return null;
        TeacherEntity te = new TeacherEntity();
        te.setIdTeacher(teacher.getIdTeacher());
        te.setName(teacher.getName());
        return te;
    }

    @Named("mapTeacherToDomain")
    default Teacher mapTeacherToDomain(TeacherEntity entity) {
        if (entity == null) return null;
        Teacher t = new Teacher();
        t.setIdTeacher(entity.getIdTeacher());
        t.setName(entity.getName());
        return t;
    }

}
