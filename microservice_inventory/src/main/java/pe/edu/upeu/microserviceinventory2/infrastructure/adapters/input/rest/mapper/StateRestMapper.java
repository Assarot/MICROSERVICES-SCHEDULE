package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pe.edu.upeu.microserviceinventory2.domain.model.State;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.request.StateCreateRequest;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.response.StateResponse;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StateRestMapper {

    @Mapping(target = "idState", ignore = true)
    State toState(StateCreateRequest request);

    StateResponse toStateResponse(State state);

    List<StateResponse> toStateResponseList(List<State> states);
}
