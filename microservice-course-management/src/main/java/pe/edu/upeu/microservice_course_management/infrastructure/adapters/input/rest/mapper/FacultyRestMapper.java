package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pe.edu.upeu.microservice_course_management.domain.model.Faculty;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request.FacultyCreateRequest;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.respose.FacultyResponse;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface FacultyRestMapper {
    Faculty toFaculty(FacultyCreateRequest request);
    FacultyResponse toFacultyResponse(Faculty faculty);
    List<FacultyResponse> toFacultyResponseList(List<Faculty> facultyList);
}
