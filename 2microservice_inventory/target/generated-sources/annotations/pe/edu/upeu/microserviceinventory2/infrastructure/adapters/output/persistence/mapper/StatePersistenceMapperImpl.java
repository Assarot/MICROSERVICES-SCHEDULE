package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microserviceinventory2.domain.model.State;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.StateEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-10T23:48:35-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251023-0518, environment: Java 21.0.8 (Eclipse Adoptium)"
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
        stateEntity.isActive( state.getIsActive() );
        stateEntity.name( state.getName() );

        return stateEntity.build();
    }

    @Override
    public State toState(StateEntity entity) {
        if ( entity == null ) {
            return null;
        }

        State.StateBuilder state = State.builder();

        state.idState( entity.getIdState() );
        state.isActive( entity.getIsActive() );
        state.name( entity.getName() );

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
