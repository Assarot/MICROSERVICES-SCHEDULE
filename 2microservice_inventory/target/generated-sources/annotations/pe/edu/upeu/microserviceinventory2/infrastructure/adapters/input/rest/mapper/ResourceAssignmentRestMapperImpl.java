package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microserviceinventory2.domain.model.ResourceAssignment;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.request.ResourceAssignmentRequest;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.response.ResourceAssignmentResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-10T23:36:18-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251023-0518, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class ResourceAssignmentRestMapperImpl implements ResourceAssignmentRestMapper {

    @Override
    public ResourceAssignment toModel(ResourceAssignmentRequest request) {
        if ( request == null ) {
            return null;
        }

        ResourceAssignment.ResourceAssignmentBuilder resourceAssignment = ResourceAssignment.builder();

        resourceAssignment.idAcademicSpace( request.getIdAcademicSpace() );
        resourceAssignment.idResource( request.getIdResource() );

        return resourceAssignment.build();
    }

    @Override
    public ResourceAssignmentResponse toResponse(ResourceAssignment model) {
        if ( model == null ) {
            return null;
        }

        ResourceAssignmentResponse resourceAssignmentResponse = new ResourceAssignmentResponse();

        resourceAssignmentResponse.setIdAcademicSpace( model.getIdAcademicSpace() );
        resourceAssignmentResponse.setIdResourceAssignment( model.getIdResourceAssignment() );
        resourceAssignmentResponse.setResource( model.getResource() );

        return resourceAssignmentResponse;
    }

    @Override
    public List<ResourceAssignmentResponse> toResponseList(List<ResourceAssignment> models) {
        if ( models == null ) {
            return null;
        }

        List<ResourceAssignmentResponse> list = new ArrayList<ResourceAssignmentResponse>( models.size() );
        for ( ResourceAssignment resourceAssignment : models ) {
            list.add( toResponse( resourceAssignment ) );
        }

        return list;
    }
}
