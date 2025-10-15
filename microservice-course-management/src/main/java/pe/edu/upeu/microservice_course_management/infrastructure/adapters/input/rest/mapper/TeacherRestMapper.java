package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pe.edu.upeu.microservice_course_management.domain.model.Teacher;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request.TeacherCreateRequest;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.respose.TeacherResponse;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface TeacherRestMapper {
    Teacher toTeacher(TeacherCreateRequest request);
    TeacherResponse toTeacherResponse(Teacher teacher);
    List<TeacherResponse> toTeacherResponseList(List<Teacher> teacherList);
}
