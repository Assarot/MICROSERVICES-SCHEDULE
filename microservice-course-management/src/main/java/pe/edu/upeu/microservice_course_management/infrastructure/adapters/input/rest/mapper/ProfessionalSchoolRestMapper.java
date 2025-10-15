package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import pe.edu.upeu.microservice_course_management.domain.model.Cycle;
import pe.edu.upeu.microservice_course_management.domain.model.Faculty;
import pe.edu.upeu.microservice_course_management.domain.model.ProfessionalSchool;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request.ProfessionalSchoolCreateRequest;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.respose.ProfessionalSchoolResponse;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProfessionalSchoolRestMapper {

    @Mapping(target = "faculty", source = "idFaculty", qualifiedByName = "mapIdToFaculty")
    ProfessionalSchool toProfessionalSchool(ProfessionalSchoolCreateRequest request);

    ProfessionalSchoolResponse toProfessionalSchoolResponse(ProfessionalSchool professionalSchool);
    List<ProfessionalSchoolResponse> toProfessionalSchoolResponseList(List<ProfessionalSchool> professionalSchools);

    @Named("mapIdToFaculty")
    default Faculty mapIdToFaculty(Long idFaculty) {
        if (idFaculty == null) return null;
        Faculty faculty = new Faculty();
        faculty.setIdFaculty(idFaculty);
        return faculty;
    }
}
