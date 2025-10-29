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
    date = "2025-10-29T13:34:13-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class ResourcePersistenceMapperImpl implements ResourcePersistenceMapper {

    @Override
    public ResourceEntity toEntity(Resource model) {
        if ( model == null ) {
            return null;
        }

        ResourceEntity.ResourceEntityBuilder resourceEntity = ResourceEntity.builder();

        resourceEntity.idResource( model.getIdResource() );
        resourceEntity.code( model.getCode() );
        resourceEntity.stock( model.getStock() );
        resourceEntity.resourcePhotoUrl( model.getResourcePhotoUrl() );
        resourceEntity.observation( model.getObservation() );
        resourceEntity.idResourceType( model.getIdResourceType() );
        resourceEntity.idState( model.getIdState() );
        resourceEntity.resourceType( resourceTypeToResourceTypeEntity( model.getResourceType() ) );
        resourceEntity.state( stateToStateEntity( model.getState() ) );

        return resourceEntity.build();
    }

    @Override
    public Resource toModel(ResourceEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Resource.ResourceBuilder resource = Resource.builder();

        resource.idResource( entity.getIdResource() );
        resource.code( entity.getCode() );
        resource.stock( entity.getStock() );
        resource.resourcePhotoUrl( entity.getResourcePhotoUrl() );
        resource.observation( entity.getObservation() );
        resource.idResourceType( entity.getIdResourceType() );
        resource.idState( entity.getIdState() );
        resource.resourceType( resourceTypeEntityToResourceType( entity.getResourceType() ) );
        resource.state( stateEntityToState( entity.getState() ) );

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
}
