package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pe.edu.upeu.microservice_course_management.domain.model.CourseType;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request.CourseTypeCreateRequest;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.respose.CourseTypeResponse;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CourseTypeRestMapper {

    CourseType toCourseType(CourseTypeCreateRequest request);
    CourseTypeResponse toCourseTypeResponse(CourseType courseType);
    List<CourseTypeResponse> toCourseTypeResposeList(List<CourseType> courseTypeList);

}
