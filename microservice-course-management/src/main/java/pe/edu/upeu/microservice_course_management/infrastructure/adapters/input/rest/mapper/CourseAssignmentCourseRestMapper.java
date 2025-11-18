package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import pe.edu.upeu.microservice_course_management.domain.model.Course;
import pe.edu.upeu.microservice_course_management.domain.model.CourseAssignment;
import pe.edu.upeu.microservice_course_management.domain.model.CourseAssignmentCourse;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request.CourseAssignmentCourseCreateRequest;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.respose.CourseAssignmentCourseResponse;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CourseAssignmentCourseRestMapper {
    @Mapping(target = "course", source = "idCourse", qualifiedByName = "mapIdToCourse")
    @Mapping(target = "courseAssignment", source = "idCourseAssignment", qualifiedByName = "mapIdToCourseAssignment")
    CourseAssignmentCourse toCourseAssignmentCourse(CourseAssignmentCourseCreateRequest request);

    @Mapping(target = "course", source = "course")
    @Mapping(target = "courseAssignment", source = "courseAssignment")
    CourseAssignmentCourseResponse toCourseAssignmentCourseResponse(CourseAssignmentCourse courseAssignmentCourse);
    List<CourseAssignmentCourseResponse> toCourseAssignmentCourseResponseList(List<CourseAssignmentCourse> courseAssignmentCourse);

    @Named("mapIdToCourse")
    default Course mapIdToCourse(Long idCourse) {
        if (idCourse== null) return null;
        Course course = new Course();
        course.setIdCourse(idCourse);
        return course;
    }

    @Named("mapIdToCourseAssignment")
    default CourseAssignment mapIdToCourseAssignment(Long id) {
        if (id== null) return null;
        CourseAssignment courseAssignment = new CourseAssignment();
        courseAssignment.setIdCourseAssignment(id);
        return courseAssignment;
    }
}
