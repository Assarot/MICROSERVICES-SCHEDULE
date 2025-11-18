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
    date = "2025-11-18T10:09:56-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251023-0518, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class ResourceTypeRestMapperImpl implements ResourceTypeRestMapper {

    @Override
    public ResourceType toModel(ResourceTypeRequest request) {
        if ( request == null ) {
            return null;
        }

        ResourceType.ResourceTypeBuilder resourceType = ResourceType.builder();

        resourceType.idCategoryResource( request.getIdCategoryResource() );
        resourceType.isActive( request.getIsActive() );
        resourceType.name( request.getName() );

        return resourceType.build();
    }

    @Override
    public ResourceTypeResponse toResponse(ResourceType model) {
        if ( model == null ) {
            return null;
        }

        ResourceTypeResponse resourceTypeResponse = new ResourceTypeResponse();

        resourceTypeResponse.setCategoryResource( model.getCategoryResource() );
        resourceTypeResponse.setIdResourceType( model.getIdResourceType() );
        resourceTypeResponse.setIsActive( model.getIsActive() );
        resourceTypeResponse.setName( model.getName() );

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
