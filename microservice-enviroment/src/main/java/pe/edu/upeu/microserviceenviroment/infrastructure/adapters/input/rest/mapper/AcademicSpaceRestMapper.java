package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import pe.edu.upeu.microserviceenviroment.domain.model.AcademicSpace;
import pe.edu.upeu.microserviceenviroment.domain.model.Floor;
import pe.edu.upeu.microserviceenviroment.domain.model.State;
import pe.edu.upeu.microserviceenviroment.domain.model.TypeAcademicSpace;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.request.AcademicSpaceCreateRequest;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.response.AcademicSpaceResponse;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AcademicSpaceRestMapper {
    @Mapping(target = "state", source = "idState", qualifiedByName = "mapIdState")
    @Mapping(target = "floor", source = "idFloor", qualifiedByName = "mapIdFloor")
    @Mapping(target = "typeAcademicSpace", source = "idTypeAcademicSpace", qualifiedByName = "mapIdTypeAcademicSpace")
    AcademicSpace toAcademicSpace(AcademicSpaceCreateRequest request);
    AcademicSpaceResponse toAcademicSpaceResponse(AcademicSpace academicSpace);
    List<AcademicSpaceResponse> toAcademicSpaceResponseList(List<AcademicSpace> academicSpaces);

    @Named("mapIdState")
    default State mapIdState(Long idState) {
        if (idState == null) return null;
        State state = new State();
        state.setIdState(idState);
        return state;
    }

    @Named("mapIdFloor")
    default Floor mapIdFloor(Long idFloor) {
        if (idFloor == null) return null;
        Floor floor = new Floor();
        floor.setIdFloor(idFloor);
        return floor;
    }

    @Named("mapIdTypeAcademicSpace")
    default TypeAcademicSpace mapIdTypeAcademicSpace(Long idTypeAcademicSpace) {
        if (idTypeAcademicSpace == null) return null;
        TypeAcademicSpace typeAcademicSpace = new TypeAcademicSpace();
        typeAcademicSpace.setIdTypeAcademicSpace(idTypeAcademicSpace);
        return typeAcademicSpace;
    }
}
