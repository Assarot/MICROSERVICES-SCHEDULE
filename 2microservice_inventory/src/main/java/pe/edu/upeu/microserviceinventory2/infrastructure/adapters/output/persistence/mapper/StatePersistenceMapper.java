package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pe.edu.upeu.microserviceinventory2.domain.model.State;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.StateEntity;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StatePersistenceMapper {
    StateEntity toStateEntity(State state);
    State toState(StateEntity entity);
    List<State> toStateList(List<StateEntity> entities);
}
