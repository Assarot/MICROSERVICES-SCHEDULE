package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import pe.edu.upeu.microservice_course_management.domain.model.Cycle;
import pe.edu.upeu.microservice_course_management.domain.model.Group;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request.GroupCreateRequest;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.respose.GroupResponse;

import java.util.List;

@Mapper(componentModel = "spring",unmappedSourcePolicy = ReportingPolicy.IGNORE,
uses = {CycleRestMapper.class})
public interface GroupRestMapper {
    // 👇 aquí le decimos cómo mapear el id_cycle hacia un objeto Cycle
    @Mapping(target = "cycle", source = "idCycle", qualifiedByName = "mapIdToCycle")
    Group toGroup(GroupCreateRequest request);

    GroupResponse toGroupResponse(Group group);
    List<GroupResponse> toGroupResponseList(List<Group> groups);

    // ========= Método auxiliar para MapStruct =========
    @Named("mapIdToCycle")
    default Cycle mapIdToCycle(Long idCycle) {
        if (idCycle == null) return null;
        Cycle cycle = new Cycle();
        cycle.setIdCycle(idCycle);
        return cycle;
    }
}
