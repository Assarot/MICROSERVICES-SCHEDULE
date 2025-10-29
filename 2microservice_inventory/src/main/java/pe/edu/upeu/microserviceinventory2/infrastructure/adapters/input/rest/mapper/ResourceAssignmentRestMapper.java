package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pe.edu.upeu.microserviceinventory2.domain.model.ResourceAssignment;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.request.ResourceAssignmentRequest;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.response.ResourceAssignmentResponse;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResourceAssignmentRestMapper {

    @Mapping(target = "idResourceAssignment", ignore = true)
    @Mapping(target = "resource", ignore = true)
    ResourceAssignment toModel(ResourceAssignmentRequest request);

    ResourceAssignmentResponse toResponse(ResourceAssignment model);

    List<ResourceAssignmentResponse> toResponseList(List<ResourceAssignment> models);
}
