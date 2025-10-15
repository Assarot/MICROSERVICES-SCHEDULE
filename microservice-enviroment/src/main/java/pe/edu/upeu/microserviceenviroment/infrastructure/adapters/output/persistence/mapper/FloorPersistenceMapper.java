package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pe.edu.upeu.microserviceenviroment.domain.model.Building;
import pe.edu.upeu.microserviceenviroment.domain.model.Floor;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.entity.BuildingEntity;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.entity.FloorEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FloorPersistenceMapper {
    @Mapping(target = "buildingEntity", source = "building", qualifiedByName = "mapBuildingToEntity")
    FloorEntity toFloorEntity(Floor floor);
    @Mapping(target = "building", source = "buildingEntity", qualifiedByName = "mapBuildingToDomain")
    Floor toFloor(FloorEntity entity);
    List<Floor> toFloorList(List<FloorEntity> entityList);

    @Named("mapBuildingToEntity")
    default BuildingEntity mapBuildingToEntity(Building building) {
        if (building == null) return null;
        BuildingEntity entity = new BuildingEntity();
        entity.setIdBuilding(building.getIdBuilding());
        return entity;
    }

    @Named("mapBuildingToDomain")
    default Building mapBuildingToDomain(BuildingEntity entity) {
        if (entity == null) return null;
        Building domain = new Building();
        domain.setIdBuilding(entity.getIdBuilding());
        domain.setName(entity.getName());
        domain.setIsActive(entity.getIsActive());
        return domain;
    }
}
