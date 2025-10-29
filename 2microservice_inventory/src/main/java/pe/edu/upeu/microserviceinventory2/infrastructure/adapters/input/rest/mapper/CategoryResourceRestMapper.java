package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pe.edu.upeu.microserviceinventory2.domain.model.CategoryResource;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.request.CategoryResourceRequest;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.response.CategoryResourceResponse;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryResourceRestMapper {

    @Mapping(target = "idCategoryResource", ignore = true)
    CategoryResource toModel(CategoryResourceRequest request);

    CategoryResourceResponse toResponse(CategoryResource model);

    List<CategoryResourceResponse> toResponseList(List<CategoryResource> models);
}
