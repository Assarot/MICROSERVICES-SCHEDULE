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
    date = "2025-10-29T07:44:28-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251001-1143, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class ResourceAssignmentPersistenceMapperImpl implements ResourceAssignmentPersistenceMapper {

    @Override
    public ResourceAssignmentEntity toEntity(ResourceAssignment model) {
        if ( model == null ) {
            return null;
        }

        ResourceAssignmentEntity.ResourceAssignmentEntityBuilder resourceAssignmentEntity = ResourceAssignmentEntity.builder();

        resourceAssignmentEntity.idAcademicSpace( model.getIdAcademicSpace() );
        resourceAssignmentEntity.idResource( model.getIdResource() );
        resourceAssignmentEntity.idResourceAssignment( model.getIdResourceAssignment() );
        resourceAssignmentEntity.resource( resourceToResourceEntity( model.getResource() ) );

        return resourceAssignmentEntity.build();
    }

    @Override
    public ResourceAssignment toModel(ResourceAssignmentEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ResourceAssignment.ResourceAssignmentBuilder resourceAssignment = ResourceAssignment.builder();

        resourceAssignment.idAcademicSpace( entity.getIdAcademicSpace() );
        resourceAssignment.idResource( entity.getIdResource() );
        resourceAssignment.idResourceAssignment( entity.getIdResourceAssignment() );
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
        categoryResourceEntity.isActive( categoryResource.getIsActive() );
        categoryResourceEntity.name( categoryResource.getName() );

        return categoryResourceEntity.build();
    }

    protected ResourceTypeEntity resourceTypeToResourceTypeEntity(ResourceType resourceType) {
        if ( resourceType == null ) {
            return null;
        }

        ResourceTypeEntity.ResourceTypeEntityBuilder resourceTypeEntity = ResourceTypeEntity.builder();

        resourceTypeEntity.categoryResource( categoryResourceToCategoryResourceEntity( resourceType.getCategoryResource() ) );
        resourceTypeEntity.idCategoryResource( resourceType.getIdCategoryResource() );
        resourceTypeEntity.idResourceType( resourceType.getIdResourceType() );
        resourceTypeEntity.isActive( resourceType.getIsActive() );
        resourceTypeEntity.name( resourceType.getName() );

        return resourceTypeEntity.build();
    }

    protected StateEntity stateToStateEntity(State state) {
        if ( state == null ) {
            return null;
        }

        StateEntity.StateEntityBuilder stateEntity = StateEntity.builder();

        stateEntity.idState( state.getIdState() );
        stateEntity.isActive( state.getIsActive() );
        stateEntity.name( state.getName() );

        return stateEntity.build();
    }

    protected ResourceEntity resourceToResourceEntity(Resource resource) {
        if ( resource == null ) {
            return null;
        }

        ResourceEntity.ResourceEntityBuilder resourceEntity = ResourceEntity.builder();

        resourceEntity.code( resource.getCode() );
        resourceEntity.idResource( resource.getIdResource() );
        resourceEntity.idResourceType( resource.getIdResourceType() );
        resourceEntity.idState( resource.getIdState() );
        resourceEntity.observation( resource.getObservation() );
        resourceEntity.resourcePhotoUrl( resource.getResourcePhotoUrl() );
        resourceEntity.resourceType( resourceTypeToResourceTypeEntity( resource.getResourceType() ) );
        resourceEntity.state( stateToStateEntity( resource.getState() ) );
        resourceEntity.stock( resource.getStock() );

        return resourceEntity.build();
    }

    protected CategoryResource categoryResourceEntityToCategoryResource(CategoryResourceEntity categoryResourceEntity) {
        if ( categoryResourceEntity == null ) {
            return null;
        }

        CategoryResource.CategoryResourceBuilder categoryResource = CategoryResource.builder();

        categoryResource.idCategoryResource( categoryResourceEntity.getIdCategoryResource() );
        categoryResource.isActive( categoryResourceEntity.getIsActive() );
        categoryResource.name( categoryResourceEntity.getName() );

        return categoryResource.build();
    }

    protected ResourceType resourceTypeEntityToResourceType(ResourceTypeEntity resourceTypeEntity) {
        if ( resourceTypeEntity == null ) {
            return null;
        }

        ResourceType.ResourceTypeBuilder resourceType = ResourceType.builder();

        resourceType.categoryResource( categoryResourceEntityToCategoryResource( resourceTypeEntity.getCategoryResource() ) );
        resourceType.idCategoryResource( resourceTypeEntity.getIdCategoryResource() );
        resourceType.idResourceType( resourceTypeEntity.getIdResourceType() );
        resourceType.isActive( resourceTypeEntity.getIsActive() );
        resourceType.name( resourceTypeEntity.getName() );

        return resourceType.build();
    }

    protected State stateEntityToState(StateEntity stateEntity) {
        if ( stateEntity == null ) {
            return null;
        }

        State.StateBuilder state = State.builder();

        state.idState( stateEntity.getIdState() );
        state.isActive( stateEntity.getIsActive() );
        state.name( stateEntity.getName() );

        return state.build();
    }

    protected Resource resourceEntityToResource(ResourceEntity resourceEntity) {
        if ( resourceEntity == null ) {
            return null;
        }

        Resource.ResourceBuilder resource = Resource.builder();

        resource.code( resourceEntity.getCode() );
        resource.idResource( resourceEntity.getIdResource() );
        resource.idResourceType( resourceEntity.getIdResourceType() );
        resource.idState( resourceEntity.getIdState() );
        resource.observation( resourceEntity.getObservation() );
        resource.resourcePhotoUrl( resourceEntity.getResourcePhotoUrl() );
        resource.resourceType( resourceTypeEntityToResourceType( resourceEntity.getResourceType() ) );
        resource.state( stateEntityToState( resourceEntity.getState() ) );
        resource.stock( resourceEntity.getStock() );

        return resource.build();
    }
}
