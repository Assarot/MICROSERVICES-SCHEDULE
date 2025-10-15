package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.mapper;

import org.mapstruct.Mapper;
import pe.edu.upeu.microserviceenviroment.domain.model.Building;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.entity.BuildingEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BuildingPersistenceMapper {

    BuildingEntity toBuildingEntity(Building building);
    Building toBuilding(BuildingEntity entity);
    List<Building> toBuildingList(List<BuildingEntity> entityList);
}
