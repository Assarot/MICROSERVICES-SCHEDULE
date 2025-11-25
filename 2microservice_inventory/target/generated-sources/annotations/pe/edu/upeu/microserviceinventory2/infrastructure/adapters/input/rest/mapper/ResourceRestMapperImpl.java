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
    date = "2025-11-25T14:27:56-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Amazon.com Inc.)"
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
        resource.stock( request.getStock() );
        resource.observation( request.getObservation() );
        resource.idResourceType( request.getIdResourceType() );
        resource.idState( request.getIdState() );

        return resource.build();
    }

    @Override
    public ResourceResponse toResponse(Resource model) {
        if ( model == null ) {
            return null;
        }

        ResourceResponse resourceResponse = new ResourceResponse();

        resourceResponse.setIdResource( model.getIdResource() );
        resourceResponse.setCode( model.getCode() );
        resourceResponse.setStock( model.getStock() );
        resourceResponse.setResourcePhotoUrl( model.getResourcePhotoUrl() );
        resourceResponse.setObservation( model.getObservation() );
        resourceResponse.setResourceType( model.getResourceType() );
        resourceResponse.setState( model.getState() );

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
