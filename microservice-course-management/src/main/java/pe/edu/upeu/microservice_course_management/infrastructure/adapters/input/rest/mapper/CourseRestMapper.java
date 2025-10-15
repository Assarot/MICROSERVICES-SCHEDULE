package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import pe.edu.upeu.microservice_course_management.domain.model.*;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request.CourseCreateRequest;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.respose.CourseResponse;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CourseRestMapper {

    @Mapping(target = "courseType", source = "idCourseType", qualifiedByName = "mapIdToCourseType")
    @Mapping(target = "courseMode", source = "idCourseMode", qualifiedByName = "mapIdToCourseMode")
    @Mapping(target = "plan", source = "idPlan", qualifiedByName = "mapIdToPlan")
    @Mapping(target = "group", source = "idGroup", qualifiedByName = "mapIdToGroup")
    Course toCourse(CourseCreateRequest request);
    CourseResponse toCourseResponse(Course course);
    List<CourseResponse> toCoursesResposeList(List<Course> courses);

    // Course Type:

    @Named("mapIdToCourseType")
    default CourseType mapIdToCourseType(Long idCourseType) {
        if (idCourseType == null) return null;
        CourseType courseType = new CourseType();
        courseType.setIdCourseType(idCourseType);
        return courseType;
    }

    // Course Mode

    @Named("mapIdToCourseMode")
    default CourseMode mapIdToCourseMode(Long idCourseMode) {
        if (idCourseMode == null) return null;
        CourseMode courseMode = new CourseMode();
        courseMode.setIdCourseMode(idCourseMode);
        return courseMode;
    }

    // Plan

    @Named("mapIdToPlan")
    default Plan mapIdToPlan(Long idPlan) {
        if (idPlan == null) return null;
        Plan plan = new Plan();
        plan.setIdPlan(idPlan);
        return plan;
    }

    // Group

    @Named("mapIdToGroup")
    default Group mapIdToGroup(Long idGroup) {
        if (idGroup == null) return null;
        Group group = new Group();
        group.setIdGroup(idGroup);
        return group;
    }

}
