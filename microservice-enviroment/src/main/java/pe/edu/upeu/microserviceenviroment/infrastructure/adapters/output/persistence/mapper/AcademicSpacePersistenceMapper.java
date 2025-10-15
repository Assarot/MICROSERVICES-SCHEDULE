package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pe.edu.upeu.microserviceenviroment.domain.model.AcademicSpace;
import pe.edu.upeu.microserviceenviroment.domain.model.Floor;
import pe.edu.upeu.microserviceenviroment.domain.model.State;
import pe.edu.upeu.microserviceenviroment.domain.model.TypeAcademicSpace;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.entity.AcademicSpaceEntity;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.entity.FloorEntity;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.entity.StateEntity;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.entity.TypeAcademicSpaceEntity;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        TypeAcademicSpacePersistenceMapper.class, FloorPersistenceMapper.class, StatePersistenceMapper.class
})
public interface AcademicSpacePersistenceMapper {
    @Mapping(target = "typeAcademicSpaceEntity", source = "typeAcademicSpace", qualifiedByName = "mapTypeAcademicSpaceEntity")
    @Mapping(target = "stateEntity", source = "state", qualifiedByName = "mapStateToEntity")
    @Mapping(target = "floorEntity", source = "floor", qualifiedByName = "mapFloorToEntity")
    AcademicSpaceEntity toAcademicSpaceEntity(AcademicSpace academicSpace);
    @Mapping(target = "typeAcademicSpace", source = "typeAcademicSpaceEntity", qualifiedByName = "mapTypeAcademicSpaceToDomain")
    @Mapping(target = "state", source = "stateEntity", qualifiedByName = "mapStateToDomain")
    @Mapping(target = "floor", source = "floorEntity", qualifiedByName = "mapFloorToDomain")
    AcademicSpace toAcademicSpace(AcademicSpaceEntity entity);
    List<AcademicSpace> toAcademicSpaceList(List<AcademicSpaceEntity> entityList);

    @Named("mapTypeAcademicSpaceEntity")
    default TypeAcademicSpaceEntity mapTypeAcademicSpaceEntity(TypeAcademicSpace typeAcademicSpace) {
        if (typeAcademicSpace == null) return null;
        TypeAcademicSpaceEntity entity = new TypeAcademicSpaceEntity();
        entity.setIdTypeAcademicSpace(typeAcademicSpace.getIdTypeAcademicSpace());
        return entity;
    }

    @Named("mapStateToEntity")
    default StateEntity mapStateToEntity(State state) {
        if (state == null) return null;
        StateEntity entity = new StateEntity();
        entity.setIdState(state.getIdState());
        return entity;
    }

    @Named("mapFloorToEntity")
    default FloorEntity mapFloorToEntity(Floor floor) {
        if (floor == null) return null;
        FloorEntity entity = new FloorEntity();
        entity.setIdFloor(floor.getIdFloor());
        return entity;
    }

    @Named("mapTypeAcademicSpaceToDomain")
    default TypeAcademicSpace mapTypeAcademicSpaceToDomain(TypeAcademicSpaceEntity entity) {
        if (entity == null) return null;
        TypeAcademicSpace domain = new TypeAcademicSpace();
        domain.setIdTypeAcademicSpace(entity.getIdTypeAcademicSpace());
        return domain;
    }

    @Named("mapStateToDomain")
    default State mapStateToDomain(StateEntity entity) {
        if (entity == null) return null;
        State domain = new State();
        domain.setIdState(entity.getIdState());
        return domain;
    }

    @Named("mapFloorToDomain")
    default Floor mapFloorToDomain(FloorEntity entity) {
        if (entity == null) return null;
        Floor domain = new Floor();
        domain.setIdFloor(entity.getIdFloor());
        return domain;
    }
}
