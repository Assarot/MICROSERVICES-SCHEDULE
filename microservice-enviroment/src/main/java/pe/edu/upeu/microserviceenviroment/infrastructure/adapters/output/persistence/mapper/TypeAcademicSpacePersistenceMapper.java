package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.mapper;

import org.mapstruct.Mapper;
import pe.edu.upeu.microserviceenviroment.domain.model.TypeAcademicSpace;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.entity.TypeAcademicSpaceEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TypeAcademicSpacePersistenceMapper {
    TypeAcademicSpaceEntity toTypeAcademicSpaceEntity(TypeAcademicSpace typeAcademicSpace);
    TypeAcademicSpace toTypeAcademicSpace(TypeAcademicSpaceEntity entity);
    List<TypeAcademicSpace> toTypeAcademicSpaceList(List<TypeAcademicSpaceEntity> entityList);
}
