package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microserviceinventory2.domain.model.State;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.request.StateCreateRequest;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.response.StateResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-29T13:34:13-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class StateRestMapperImpl implements StateRestMapper {

    @Override
    public State toState(StateCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        State.StateBuilder state = State.builder();

        state.name( request.getName() );
        state.isActive( request.getIsActive() );

        return state.build();
    }

    @Override
    public StateResponse toStateResponse(State state) {
        if ( state == null ) {
            return null;
        }

        StateResponse stateResponse = new StateResponse();

        stateResponse.setIdState( state.getIdState() );
        stateResponse.setName( state.getName() );
        stateResponse.setIsActive( state.getIsActive() );

        return stateResponse;
    }

    @Override
    public List<StateResponse> toStateResponseList(List<State> states) {
        if ( states == null ) {
            return null;
        }

        List<StateResponse> list = new ArrayList<StateResponse>( states.size() );
        for ( State state : states ) {
            list.add( toStateResponse( state ) );
        }

        return list;
    }
}
