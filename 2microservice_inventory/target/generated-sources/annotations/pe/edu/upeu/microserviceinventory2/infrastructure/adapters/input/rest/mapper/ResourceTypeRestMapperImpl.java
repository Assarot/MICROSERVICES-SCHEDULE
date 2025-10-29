package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microserviceinventory2.domain.model.ResourceType;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.request.ResourceTypeRequest;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.response.ResourceTypeResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-29T13:34:13-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class ResourceTypeRestMapperImpl implements ResourceTypeRestMapper {

    @Override
    public ResourceType toModel(ResourceTypeRequest request) {
        if ( request == null ) {
            return null;
        }

        ResourceType.ResourceTypeBuilder resourceType = ResourceType.builder();

        resourceType.name( request.getName() );
        resourceType.isActive( request.getIsActive() );
        resourceType.idCategoryResource( request.getIdCategoryResource() );

        return resourceType.build();
    }

    @Override
    public ResourceTypeResponse toResponse(ResourceType model) {
        if ( model == null ) {
            return null;
        }

        ResourceTypeResponse resourceTypeResponse = new ResourceTypeResponse();

        resourceTypeResponse.setIdResourceType( model.getIdResourceType() );
        resourceTypeResponse.setName( model.getName() );
        resourceTypeResponse.setIsActive( model.getIsActive() );
        resourceTypeResponse.setCategoryResource( model.getCategoryResource() );

        return resourceTypeResponse;
    }

    @Override
    public List<ResourceTypeResponse> toResponseList(List<ResourceType> models) {
        if ( models == null ) {
            return null;
        }

        List<ResourceTypeResponse> list = new ArrayList<ResourceTypeResponse>( models.size() );
        for ( ResourceType resourceType : models ) {
            list.add( toResponse( resourceType ) );
        }

        return list;
    }
}
