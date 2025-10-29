package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microserviceinventory2.domain.model.CategoryResource;
import pe.edu.upeu.microserviceinventory2.domain.model.Resource;
import pe.edu.upeu.microserviceinventory2.domain.model.ResourceAssignment;
import pe.edu.upeu.microserviceinventory2.domain.model.ResourceType;
import pe.edu.upeu.microserviceinventory2.domain.model.State;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.CategoryResourceEntity;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.ResourceAssignmentEntity;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.ResourceEntity;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.ResourceTypeEntity;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.StateEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-29T13:34:13-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class ResourceAssignmentPersistenceMapperImpl implements ResourceAssignmentPersistenceMapper {

    @Override
    public ResourceAssignmentEntity toEntity(ResourceAssignment model) {
        if ( model == null ) {
            return null;
        }

        ResourceAssignmentEntity.ResourceAssignmentEntityBuilder resourceAssignmentEntity = ResourceAssignmentEntity.builder();

        resourceAssignmentEntity.idResourceAssignment( model.getIdResourceAssignment() );
        resourceAssignmentEntity.idAcademicSpace( model.getIdAcademicSpace() );
        resourceAssignmentEntity.idResource( model.getIdResource() );
        resourceAssignmentEntity.resource( resourceToResourceEntity( model.getResource() ) );

        return resourceAssignmentEntity.build();
    }

    @Override
    public ResourceAssignment toModel(ResourceAssignmentEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ResourceAssignment.ResourceAssignmentBuilder resourceAssignment = ResourceAssignment.builder();

        resourceAssignment.idResourceAssignment( entity.getIdResourceAssignment() );
        resourceAssignment.idAcademicSpace( entity.getIdAcademicSpace() );
        resourceAssignment.idResource( entity.getIdResource() );
        resourceAssignment.resource( resourceEntityToResource( entity.getResource() ) );

        return resourceAssignment.build();
    }

    @Override
    public List<ResourceAssignment> toModelList(List<ResourceAssignmentEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ResourceAssignment> list = new ArrayList<ResourceAssignment>( entities.size() );
        for ( ResourceAssignmentEntity resourceAssignmentEntity : entities ) {
            list.add( toModel( resourceAssignmentEntity ) );
        }

        return list;
    }

    protected CategoryResourceEntity categoryResourceToCategoryResourceEntity(CategoryResource categoryResource) {
        if ( categoryResource == null ) {
            return null;
        }

        CategoryResourceEntity.CategoryResourceEntityBuilder categoryResourceEntity = CategoryResourceEntity.builder();

        categoryResourceEntity.idCategoryResource( categoryResource.getIdCategoryResource() );
        categoryResourceEntity.name( categoryResource.getName() );
        categoryResourceEntity.isActive( categoryResource.getIsActive() );

        return categoryResourceEntity.build();
    }

    protected ResourceTypeEntity resourceTypeToResourceTypeEntity(ResourceType resourceType) {
        if ( resourceType == null ) {
            return null;
        }

        ResourceTypeEntity.ResourceTypeEntityBuilder resourceTypeEntity = ResourceTypeEntity.builder();

        resourceTypeEntity.idResourceType( resourceType.getIdResourceType() );
        resourceTypeEntity.name( resourceType.getName() );
        resourceTypeEntity.isActive( resourceType.getIsActive() );
        resourceTypeEntity.idCategoryResource( resourceType.getIdCategoryResource() );
        resourceTypeEntity.categoryResource( categoryResourceToCategoryResourceEntity( resourceType.getCategoryResource() ) );

        return resourceTypeEntity.build();
    }

    protected StateEntity stateToStateEntity(State state) {
        if ( state == null ) {
            return null;
        }

        StateEntity.StateEntityBuilder stateEntity = StateEntity.builder();

        stateEntity.idState( state.getIdState() );
        stateEntity.name( state.getName() );
        stateEntity.isActive( state.getIsActive() );

        return stateEntity.build();
    }

    protected ResourceEntity resourceToResourceEntity(Resource resource) {
        if ( resource == null ) {
            return null;
        }

        ResourceEntity.ResourceEntityBuilder resourceEntity = ResourceEntity.builder();

        resourceEntity.idResource( resource.getIdResource() );
        resourceEntity.code( resource.getCode() );
        resourceEntity.stock( resource.getStock() );
        resourceEntity.resourcePhotoUrl( resource.getResourcePhotoUrl() );
        resourceEntity.observation( resource.getObservation() );
        resourceEntity.idResourceType( resource.getIdResourceType() );
        resourceEntity.idState( resource.getIdState() );
        resourceEntity.resourceType( resourceTypeToResourceTypeEntity( resource.getResourceType() ) );
        resourceEntity.state( stateToStateEntity( resource.getState() ) );

        return resourceEntity.build();
    }

    protected CategoryResource categoryResourceEntityToCategoryResource(CategoryResourceEntity categoryResourceEntity) {
        if ( categoryResourceEntity == null ) {
            return null;
        }

        CategoryResource.CategoryResourceBuilder categoryResource = CategoryResource.builder();

        categoryResource.idCategoryResource( categoryResourceEntity.getIdCategoryResource() );
        categoryResource.name( categoryResourceEntity.getName() );
        categoryResource.isActive( categoryResourceEntity.getIsActive() );

        return categoryResource.build();
    }

    protected ResourceType resourceTypeEntityToResourceType(ResourceTypeEntity resourceTypeEntity) {
        if ( resourceTypeEntity == null ) {
            return null;
        }

        ResourceType.ResourceTypeBuilder resourceType = ResourceType.builder();

        resourceType.idResourceType( resourceTypeEntity.getIdResourceType() );
        resourceType.name( resourceTypeEntity.getName() );
        resourceType.isActive( resourceTypeEntity.getIsActive() );
        resourceType.idCategoryResource( resourceTypeEntity.getIdCategoryResource() );
        resourceType.categoryResource( categoryResourceEntityToCategoryResource( resourceTypeEntity.getCategoryResource() ) );

        return resourceType.build();
    }

    protected State stateEntityToState(StateEntity stateEntity) {
        if ( stateEntity == null ) {
            return null;
        }

        State.StateBuilder state = State.builder();

        state.idState( stateEntity.getIdState() );
        state.name( stateEntity.getName() );
        state.isActive( stateEntity.getIsActive() );

        return state.build();
    }

    protected Resource resourceEntityToResource(ResourceEntity resourceEntity) {
        if ( resourceEntity == null ) {
            return null;
        }

        Resource.ResourceBuilder resource = Resource.builder();

        resource.idResource( resourceEntity.getIdResource() );
        resource.code( resourceEntity.getCode() );
        resource.stock( resourceEntity.getStock() );
        resource.resourcePhotoUrl( resourceEntity.getResourcePhotoUrl() );
        resource.observation( resourceEntity.getObservation() );
        resource.idResourceType( resourceEntity.getIdResourceType() );
        resource.idState( resourceEntity.getIdState() );
        resource.resourceType( resourceTypeEntityToResourceType( resourceEntity.getResourceType() ) );
        resource.state( stateEntityToState( resourceEntity.getState() ) );

        return resource.build();
    }
}
