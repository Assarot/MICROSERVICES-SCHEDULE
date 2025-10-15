package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pe.edu.upeu.microservice_course_management.domain.model.CourseMode;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request.CourseModeCreateRequest;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.respose.CourseModeResponse;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CourseModeRestMapper {

    CourseMode toCourseMode(CourseModeCreateRequest request);
    CourseModeResponse toCourseModeResponse(CourseMode courseMode);
    List<CourseModeResponse> toCourseModeResponseList(List<CourseMode> courseModeList);

}
