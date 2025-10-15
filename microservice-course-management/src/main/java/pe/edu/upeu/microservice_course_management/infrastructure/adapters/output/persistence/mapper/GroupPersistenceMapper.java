package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pe.edu.upeu.microservice_course_management.domain.model.Cycle;
import pe.edu.upeu.microservice_course_management.domain.model.Group;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.entity.CycleEntity;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.entity.GroupEntity;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CyclePersistenceMapper.class})
public interface GroupPersistenceMapper {

    @Mapping(target = "cycle", source = "cycle", qualifiedByName = "mapCycleToEntity")
    GroupEntity toGroupEntity(Group group);

    @Mapping(target = "cycle", source = "cycle", qualifiedByName = "mapCycleToDomain")
    Group toGroup(GroupEntity entity);

    List<Group> toGroupList(List<GroupEntity> entityList);

    @Named("mapCycleToEntity")
    default CycleEntity mapCycleToEntity(Cycle cycle) {
        if (cycle == null) return null;
        CycleEntity ce = new CycleEntity();
        ce.setIdCycle(cycle.getIdCycle());
        ce.setName(cycle.getName());
        return ce;
    }

    @Named("mapCycleToDomain")
    default Cycle mapCycleToDomain(CycleEntity entity) {
        if (entity == null) return null;
        Cycle c = new Cycle();
        c.setIdCycle(entity.getIdCycle());
        c.setName(entity.getName());
        return c;
    }
}
