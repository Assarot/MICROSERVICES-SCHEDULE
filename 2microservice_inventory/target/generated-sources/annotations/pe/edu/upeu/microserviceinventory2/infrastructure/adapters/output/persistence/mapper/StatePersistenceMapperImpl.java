package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microserviceinventory2.domain.model.State;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.StateEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-25T14:27:56-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Amazon.com Inc.)"
)
@Component
public class StatePersistenceMapperImpl implements StatePersistenceMapper {

    @Override
    public StateEntity toStateEntity(State state) {
        if ( state == null ) {
            return null;
        }

        StateEntity.StateEntityBuilder stateEntity = StateEntity.builder();

        stateEntity.idState( state.getIdState() );
        stateEntity.name( state.getName() );
        stateEntity.isActive( state.getIsActive() );

        return stateEntity.build();
    }

    @Override
    public State toState(StateEntity entity) {
        if ( entity == null ) {
            return null;
        }

        State.StateBuilder state = State.builder();

        state.idState( entity.getIdState() );
        state.name( entity.getName() );
        state.isActive( entity.getIsActive() );

        return state.build();
    }

    @Override
    public List<State> toStateList(List<StateEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<State> list = new ArrayList<State>( entities.size() );
        for ( StateEntity stateEntity : entities ) {
            list.add( toState( stateEntity ) );
        }

        return list;
    }
}
