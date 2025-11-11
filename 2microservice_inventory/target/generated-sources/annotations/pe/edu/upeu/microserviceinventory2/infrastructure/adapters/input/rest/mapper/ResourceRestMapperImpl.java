package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microserviceinventory2.domain.model.Resource;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.request.ResourceRequest;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.response.ResourceResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-10T23:48:35-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251023-0518, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class ResourceRestMapperImpl implements ResourceRestMapper {

    @Override
    public Resource toModel(ResourceRequest request) {
        if ( request == null ) {
            return null;
        }

        Resource.ResourceBuilder resource = Resource.builder();

        resource.code( request.getCode() );
        resource.idResourceType( request.getIdResourceType() );
        resource.idState( request.getIdState() );
        resource.observation( request.getObservation() );
        resource.stock( request.getStock() );

        return resource.build();
    }

    @Override
    public ResourceResponse toResponse(Resource model) {
        if ( model == null ) {
            return null;
        }

        ResourceResponse resourceResponse = new ResourceResponse();

        resourceResponse.setCode( model.getCode() );
        resourceResponse.setIdResource( model.getIdResource() );
        resourceResponse.setObservation( model.getObservation() );
        resourceResponse.setResourcePhotoUrl( model.getResourcePhotoUrl() );
        resourceResponse.setResourceType( model.getResourceType() );
        resourceResponse.setState( model.getState() );
        resourceResponse.setStock( model.getStock() );

        return resourceResponse;
    }

    @Override
    public List<ResourceResponse> toResponseList(List<Resource> models) {
        if ( models == null ) {
            return null;
        }

        List<ResourceResponse> list = new ArrayList<ResourceResponse>( models.size() );
        for ( Resource resource : models ) {
            list.add( toResponse( resource ) );
        }

        return list;
    }
}
