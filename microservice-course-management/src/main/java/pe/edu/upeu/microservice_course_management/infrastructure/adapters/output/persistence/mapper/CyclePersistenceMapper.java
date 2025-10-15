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
public interface CyclePersistenceMapper {

    @Mapping(target = "professionalSchool", source = "professionalSchool", qualifiedByName = "mapProfessionalSchoolToEntity")
    CycleEntity toCycleEntity(Cycle cycle);

    @Mapping(target = "professionalSchool", source = "professionalSchool", qualifiedByName = "mapProfessionalSchoolToDomain")
    Cycle toCycle(CycleEntity entity);
    List<Cycle> toCycleList(List<CycleEntity> entityList);

    @Named("mapProfessionalSchoolToEntity")
    default ProfessionalSchoolEntity mapProfessionalSchoolToEntity(ProfessionalSchool professionalSchool) {
        if (professionalSchool == null) return null;
        ProfessionalSchoolEntity pse = new ProfessionalSchoolEntity();
        pse.setIdProfessionalSchool(professionalSchool.getIdProfessionalSchool());
        pse.setName(professionalSchool.getName());
        return pse;
    }

    @Named("mapProfessionalSchoolToDomain")
    default ProfessionalSchool mapProfessionalSchoolToDomain(ProfessionalSchoolEntity entity) {
        if (entity == null) return null;
        ProfessionalSchool ps = new ProfessionalSchool();
        ps.setIdProfessionalSchool(entity.getIdProfessionalSchool());
        ps.setName(entity.getName());
        return ps;
    }

}
