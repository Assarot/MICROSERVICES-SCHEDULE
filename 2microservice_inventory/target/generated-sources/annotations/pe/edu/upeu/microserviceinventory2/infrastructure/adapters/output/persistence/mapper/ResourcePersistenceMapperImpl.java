package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microserviceinventory2.domain.model.CategoryResource;
import pe.edu.upeu.microserviceinventory2.domain.model.Resource;
import pe.edu.upeu.microserviceinventory2.domain.model.ResourceType;
import pe.edu.upeu.microserviceinventory2.domain.model.State;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.CategoryResourceEntity;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.ResourceEntity;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.ResourceTypeEntity;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.StateEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-18T10:09:56-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251023-0518, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class ResourcePersistenceMapperImpl implements ResourcePersistenceMapper {

    @Override
    public ResourceEntity toEntity(Resource model) {
        if ( model == null ) {
            return null;
        }

        ResourceEntity.ResourceEntityBuilder resourceEntity = ResourceEntity.builder();

        resourceEntity.code( model.getCode() );
        resourceEntity.idResource( model.getIdResource() );
        resourceEntity.idResourceType( model.getIdResourceType() );
        resourceEntity.idState( model.getIdState() );
        resourceEntity.observation( model.getObservation() );
        resourceEntity.resourcePhotoUrl( model.getResourcePhotoUrl() );
        resourceEntity.resourceType( resourceTypeToResourceTypeEntity( model.getResourceType() ) );
        resourceEntity.state( stateToStateEntity( model.getState() ) );
        resourceEntity.stock( model.getStock() );

        return resourceEntity.build();
    }

    @Override
    public Resource toModel(ResourceEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Resource.ResourceBuilder resource = Resource.builder();

        resource.code( entity.getCode() );
        resource.idResource( entity.getIdResource() );
        resource.idResourceType( entity.getIdResourceType() );
        resource.idState( entity.getIdState() );
        resource.observation( entity.getObservation() );
        resource.resourcePhotoUrl( entity.getResourcePhotoUrl() );
        resource.resourceType( resourceTypeEntityToResourceType( entity.getResourceType() ) );
        resource.state( stateEntityToState( entity.getState() ) );
        resource.stock( entity.getStock() );

        return resource.build();
    }

    @Override
    public List<Resource> toModelList(List<ResourceEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<Resource> list = new ArrayList<Resource>( entities.size() );
        for ( ResourceEntity resourceEntity : entities ) {
            list.add( toModel( resourceEntity ) );
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
}
