package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import pe.edu.upeu.microserviceenviroment.domain.model.Building;
import pe.edu.upeu.microserviceenviroment.domain.model.Floor;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.request.FloorCreateRequest;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.response.FloorResponse;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface FloorRestMapper {
    @Mapping(target = "building", source = "idBuilding", qualifiedByName = "mapIdBuilding")
    Floor toFloor(FloorCreateRequest request);
    FloorResponse toFloorResponse(Floor floor);
    List<FloorResponse> toFloorResponseList(List<Floor> floors);

    @Named("mapIdBuilding")
    default Building mapIdBuilding(Long idBuilding) {
        Building building = new Building();
        building.setIdBuilding(idBuilding);
        return building;
    }
}
