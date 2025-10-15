package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.mapper;

import org.mapstruct.Mapper;
import pe.edu.upeu.microserviceenviroment.domain.model.State;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.entity.StateEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StatePersistenceMapper {
    StateEntity toStateEntity(State state);
    State toState(StateEntity entity);
    List<State> toStateList(List<StateEntity> entityList);
}
