package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pe.edu.upeu.microserviceinventory2.domain.model.ResourceType;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.request.ResourceTypeRequest;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.response.ResourceTypeResponse;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResourceTypeRestMapper {

    @Mapping(target = "idResourceType", ignore = true)
    ResourceType toModel(ResourceTypeRequest request);

    ResourceTypeResponse toResponse(ResourceType model);

    List<ResourceTypeResponse> toResponseList(List<ResourceType> models);
}
