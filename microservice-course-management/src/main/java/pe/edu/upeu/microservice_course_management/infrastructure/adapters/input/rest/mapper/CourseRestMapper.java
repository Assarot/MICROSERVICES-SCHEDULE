package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import pe.edu.upeu.microservice_course_management.domain.model.*;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request.CourseCreateRequest;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.respose.CourseResponse;

import java.time.Duration;
import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CourseRestMapper {
    @Mapping(target = "courseType", source = "idCourseType", qualifiedByName = "mapIdToCourseType")
    @Mapping(target = "plan", source = "idPlan", qualifiedByName = "mapIdToPlan")
    @Mapping(target = "group", source = "idGroup", qualifiedByName = "mapIdToGroup")
    @Mapping(target = "duration", source = "duration", qualifiedByName = "mapDuration")
    @Mapping(target = "theoreticalHours", source = "theoreticalHours", qualifiedByName = "mapTheoreticalHours")
    @Mapping(target = "practicalHours", source = "practicalHours", qualifiedByName = "mapPracticalHours")
    Course toCourse(CourseCreateRequest request);
    CourseResponse toCourseResponse(Course course);
    List<CourseResponse> toCoursesResposeList(List<Course> courses);

    // Course Type:

    @Named("mapDuration")
    default Duration mapDuration(int value) {
        return Duration.ofMinutes(value); // O cambia a ofSeconds, dependiendo de tu caso
    }

    // Conversion de int a Duration para theoreticalHours
    @Named("mapTheoreticalHours")
    default Duration mapTheoreticalHours(int value) {
        return Duration.ofMinutes(value); // O cambia a ofSeconds si es en segundos
    }

    // Conversion de int a Duration para practicalHours
    @Named("mapPracticalHours")
    default Duration mapPracticalHours(int value) {
        return Duration.ofMinutes(value); // O cambia a ofSeconds si es en segundos
    }

    @Named("mapIdToCourseType")
    default CourseType mapIdToCourseType(Long idCourseType) {
        if (idCourseType == null) return null;
        CourseType courseType = new CourseType();
        courseType.setIdCourseType(idCourseType);
        return courseType;
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
