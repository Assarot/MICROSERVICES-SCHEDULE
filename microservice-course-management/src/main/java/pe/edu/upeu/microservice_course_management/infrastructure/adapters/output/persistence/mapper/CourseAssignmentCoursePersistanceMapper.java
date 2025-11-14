package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pe.edu.upeu.microservice_course_management.domain.model.Course;
import pe.edu.upeu.microservice_course_management.domain.model.CourseAssignment;
import pe.edu.upeu.microservice_course_management.domain.model.CourseAssignmentCourse;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.entity.CourseAssignmentCourseEntity;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.entity.CourseAssignmentEntity;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.entity.CourseEntity;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CourseAssignmentPersistenceMapper.class, CoursePersistenceMapper.class})
public interface CourseAssignmentCoursePersistanceMapper {
    @Mapping(target = "course", source = "course", qualifiedByName = "mapCourseToEntity") // Mapeo de la relación Course
    @Mapping(target = "courseAssignment", source = "courseAssignment", qualifiedByName = "mapCourseAssignmentToEntity")
    CourseAssignmentCourseEntity toCourseAssignmentCourseToEntity(CourseAssignmentCourse courseAssignmentCourse);

    @Mapping(target = "course", source = "course", qualifiedByName = "mapCourseToDomain") // Mapeo de la relación Course
    @Mapping(target = "courseAssignment", source = "courseAssignment", qualifiedByName = "mapCourseAssignmentToDomain")
    CourseAssignmentCourse toCourseAssignmentCourse(CourseAssignmentCourseEntity entity);

    List<CourseAssignmentCourse> toCourseAssignmentCourseList(List<CourseAssignmentCourseEntity> entityList);

    @Named("mapCourseAssignmentToEntity")
    default CourseAssignmentEntity mapCourseAssignmentToEntity(CourseAssignment courseAssignment){
        if(courseAssignment == null) return null;
        CourseAssignmentEntity cae = new CourseAssignmentEntity();
        cae.setIdCourseAssignment(courseAssignment.getIdCourseAssignment());
        return cae;
    }

    @Named("mapCourseAssignmentToDomain")
    default CourseAssignment mapCourseAssignmentToDomain(CourseAssignmentEntity entity){
        if(entity == null) return null;
        CourseAssignment ca = new CourseAssignment();
        ca.setIdCourseAssignment(entity.getIdCourseAssignment());
        return ca;
    }

    @Named("mapCourseToEntity")
    default CourseEntity mapCourseToEntity(Course course){
        if(course == null) return null;
        CourseEntity ce = new CourseEntity();
        ce.setIdCourse(course.getIdCourse());
        ce.setName(course.getName());
        ce.setCode(course.getCode());
        ce.setDescription(course.getDescription());
        ce.setDuration(course.getDuration());
        ce.setTheoreticalHours(course.getTheoreticalHours());
        ce.setPracticalHours(course.getPracticalHours());
        ce.setTotalHours(course.getTotalHours());
        return ce;
    }

    @Named("mapCourseToDomain")
    default Course mapCourseToDomain(CourseEntity entity){
        if(entity == null) return null;
        Course c = new Course();
        c.setIdCourse(entity.getIdCourse());
        c.setName(entity.getName());
        c.setCode(entity.getCode());
        c.setDescription(entity.getDescription());
        c.setDuration(entity.getDuration());
        c.setTheoreticalHours(entity.getTheoreticalHours());
        c.setPracticalHours(entity.getPracticalHours());
        c.setTotalHours(entity.getTotalHours());
        return c;
    }
}
