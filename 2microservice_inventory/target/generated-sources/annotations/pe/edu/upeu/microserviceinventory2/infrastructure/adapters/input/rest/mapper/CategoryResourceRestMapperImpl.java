package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microserviceinventory2.domain.model.CategoryResource;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.request.CategoryResourceRequest;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.response.CategoryResourceResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-18T10:09:56-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251023-0518, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class CategoryResourceRestMapperImpl implements CategoryResourceRestMapper {

    @Override
    public CategoryResource toModel(CategoryResourceRequest request) {
        if ( request == null ) {
            return null;
        }

        CategoryResource.CategoryResourceBuilder categoryResource = CategoryResource.builder();

        categoryResource.isActive( request.getIsActive() );
        categoryResource.name( request.getName() );

        return categoryResource.build();
    }

    @Override
    public CategoryResourceResponse toResponse(CategoryResource model) {
        if ( model == null ) {
            return null;
        }

        CategoryResourceResponse categoryResourceResponse = new CategoryResourceResponse();

        categoryResourceResponse.setIdCategoryResource( model.getIdCategoryResource() );
        categoryResourceResponse.setIsActive( model.getIsActive() );
        categoryResourceResponse.setName( model.getName() );

        return categoryResourceResponse;
    }

    @Override
    public List<CategoryResourceResponse> toResponseList(List<CategoryResource> models) {
        if ( models == null ) {
            return null;
        }

        List<CategoryResourceResponse> list = new ArrayList<CategoryResourceResponse>( models.size() );
        for ( CategoryResource categoryResource : models ) {
            list.add( toResponse( categoryResource ) );
        }

        return list;
    }
}
