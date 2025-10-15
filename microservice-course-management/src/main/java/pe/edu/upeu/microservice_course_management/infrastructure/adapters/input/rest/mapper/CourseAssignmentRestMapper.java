package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import pe.edu.upeu.microservice_course_management.domain.model.*;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request.CourseAssignmentCreateRequest;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.respose.CourseAssignmentResponse;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CourseAssignmentRestMapper {

    @Mapping(target = "course", source = "idCourse", qualifiedByName = "mapIdToCourse")
    @Mapping(target = "teacher", source = "idTeacher", qualifiedByName = "mapIdToTeacher")
    CourseAssignment toCourseAssignment(CourseAssignmentCreateRequest request);
    CourseAssignmentResponse toCourseAssignmentResponse(CourseAssignment courseAssignment);
    List<CourseAssignmentResponse> toCourseAssignmentResponseList(List<CourseAssignment> courseAssignmentList);

    // Course

    @Named("mapIdToCourse")
    default Course mapIdToCourse(Long idCourse) {
        if (idCourse == null) return null;
        Course course = new Course();
        course.setIdCourse(idCourse);
        return course;
    }

    // Teacher

    @Named("mapIdToTeacher")
    default Teacher mapIdToTeacher(Long idTeacher) {
        if (idTeacher == null) return null;
        Teacher teacher = new Teacher();
        teacher.setIdTeacher(idTeacher);
        return teacher;
    }

}
