package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pe.edu.upeu.microservice_course_management.domain.model.Cycle;
import pe.edu.upeu.microservice_course_management.domain.model.Faculty;
import pe.edu.upeu.microservice_course_management.domain.model.ProfessionalSchool;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.entity.CycleEntity;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.entity.FacultyEntity;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.entity.ProfessionalSchoolEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfessionalSchoolPersistenceMapper {

    @Mapping(target = "faculty", source = "faculty", qualifiedByName = "mapFacultyToEntity")
    ProfessionalSchoolEntity toProfessionalSchoolEntity(ProfessionalSchool professionalSchool);

    @Mapping(target = "faculty", source = "faculty", qualifiedByName = "mapFacultyToDomain")
    ProfessionalSchool toProfessionalSchool(ProfessionalSchoolEntity entity);
    List<ProfessionalSchool> toProfessionalSchoolList(List<ProfessionalSchoolEntity> entityList);

    @Named("mapFacultyToEntity")
    default FacultyEntity mapFacultyToEntity(Faculty faculty) {
        if (faculty == null) return null;
        FacultyEntity ce = new FacultyEntity();
        ce.setIdFaculty(faculty.getIdFaculty());
        ce.setName(faculty.getName());
        return ce;
    }

    @Named("mapFacultyToDomain")
    default Faculty mapFacultyToDomain(FacultyEntity entity) {
        if (entity == null) return null;
        Faculty f = new Faculty();
        f.setIdFaculty(entity.getIdFaculty());
        f.setName(entity.getName());
        return f;
    }
}
