package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pe.edu.upeu.microserviceinventory2.domain.model.Resource;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.request.ResourceRequest;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.response.ResourceResponse;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResourceRestMapper {

    @Mapping(target = "idResource", ignore = true)
    @Mapping(target = "resourcePhotoUrl", ignore = true)
    @Mapping(target = "resourceType", ignore = true)
    @Mapping(target = "state", ignore = true)
    Resource toModel(ResourceRequest request);

    ResourceResponse toResponse(Resource model);

    List<ResourceResponse> toResponseList(List<Resource> models);
}
