package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pe.edu.upeu.microserviceenviroment.domain.model.Building;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.request.BuildingCreateRequest;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.response.BuildingResponse;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface BuildingRestMapper {
    Building toBuilding(BuildingCreateRequest request);
    BuildingResponse toBuildingResponse(Building building);
    List<BuildingResponse> toBuildingResponseList(List<Building> buildings);
}
