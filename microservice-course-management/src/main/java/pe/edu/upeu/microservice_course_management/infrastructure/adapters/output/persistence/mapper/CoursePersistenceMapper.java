package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pe.edu.upeu.microservice_course_management.domain.model.*;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.entity.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CourseTypePersistenceMapper.class,
        CourseModePersistenceMapper.class,
        GroupPersistenceMapper.class,
        PlanPersistenceMapper.class})
public interface CoursePersistenceMapper {

    @Mapping(target = "courseType", source = "courseType", qualifiedByName = "mapCourseTypeToEntity")
    @Mapping(target = "courseMode", source = "courseMode", qualifiedByName = "mapCourseModeToEntity")
    @Mapping(target = "plan", source = "plan", qualifiedByName = "mapPlanToEntity")
    @Mapping(target = "group", source = "group", qualifiedByName = "mapGroupToEntity")
    CourseEntity toCourseEntity(Course course);

    @Mapping(target = "courseType", source = "courseType", qualifiedByName = "mapCourseTypeToDomain")
    @Mapping(target = "courseMode", source = "courseMode", qualifiedByName = "mapCourseModeToDomain")
    @Mapping(target = "plan", source = "plan", qualifiedByName = "mapPlanToDomain")
    @Mapping(target = "group", source = "group", qualifiedByName = "mapGroupToDomain")
    Course toCourse(CourseEntity entity);
    List<Course> toCourseList(List<CourseEntity> entityList);

    //CourseType

    @Named("mapCourseTypeToEntity")
    default CourseTypeEntity mapCourseTypeToEntity(CourseType courseType) {
        if (courseType == null) return null;
        CourseTypeEntity cte = new CourseTypeEntity();
        cte.setIdCourseType(courseType.getIdCourseType());
        cte.setName(courseType.getName());
        return cte;
    }

    @Named("mapCourseTypeToDomain")
    default CourseType mapCourseTypeToDomain(CourseTypeEntity entity) {
        if (entity == null) return null;
        CourseType ct = new CourseType();
        ct.setIdCourseType(entity.getIdCourseType());
        ct.setName(entity.getName());
        return ct;
    }

    //CourseMode

    @Named("mapCourseModeToEntity")
    default CourseModeEntity mapCourseModeToEntity(CourseMode courseMode) {
        if (courseMode == null) return null;
        CourseModeEntity cme = new CourseModeEntity();
        cme.setIdCourseMode(courseMode.getIdCourseMode());
        cme.setName(courseMode.getName());
        return cme;
    }

    @Named("mapCourseModeToDomain")
    default CourseMode mapCourseModeToDomain(CourseModeEntity entity) {
        if (entity == null) return null;
        CourseMode cm = new CourseMode();
        cm.setIdCourseMode(entity.getIdCourseMode());
        cm.setName(entity.getName());
        return cm;
    }

    // Plan

    @Named("mapPlanToEntity")
    default PlanEntity mapPlanToEntity(Plan plan) {
        if (plan == null) return null;
        PlanEntity pe = new PlanEntity();
        pe.setIdPlan(plan.getIdPlan());
        pe.setName(plan.getName());
        return pe;
    }

    @Named("mapPlanToDomain")
    default Plan mapPlanToDomain(PlanEntity entity) {
        if (entity == null) return null;
        Plan p = new Plan();
        p.setIdPlan(entity.getIdPlan());
        p.setName(entity.getName());
        return p;
    }

    // Group

    @Named("mapGroupToEntity")
    default GroupEntity mapGroupToEntity(Group group) {
        if (group == null) return null;
        GroupEntity ge = new GroupEntity();
        ge.setIdGroup(group.getIdGroup());
        ge.setGroupNumber(group.getGroupNumber());
        ge.setCapacity(group.getCapacity());
        return ge;
    }

    @Named("mapGroupToDomain")
    default Group mapGroupToDomain(GroupEntity entity) {
        if (entity == null) return null;
        Group g = new Group();
        g.setIdGroup(entity.getIdGroup());
        g.setGroupNumber(entity.getGroupNumber());
        g.setCapacity(entity.getCapacity());
        return g;
    }

}
