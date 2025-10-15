package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pe.edu.upeu.microserviceenviroment.domain.model.State;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.request.StateCreateRequest;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.response.StateResponse;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface StateRestMapper {
    State toState(StateCreateRequest request);
    StateResponse toStateResponse(State state);
    List<StateResponse> toStateResponseList(List<State> states);
}
