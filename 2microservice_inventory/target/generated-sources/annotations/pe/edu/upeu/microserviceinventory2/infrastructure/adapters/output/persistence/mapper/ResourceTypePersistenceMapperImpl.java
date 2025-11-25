package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microserviceinventory2.domain.model.CategoryResource;
import pe.edu.upeu.microserviceinventory2.domain.model.ResourceType;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.CategoryResourceEntity;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.ResourceTypeEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-25T14:27:56-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Amazon.com Inc.)"
)
@Component
public class ResourceTypePersistenceMapperImpl implements ResourceTypePersistenceMapper {

    @Override
    public ResourceTypeEntity toEntity(ResourceType model) {
        if ( model == null ) {
            return null;
        }

        ResourceTypeEntity.ResourceTypeEntityBuilder resourceTypeEntity = ResourceTypeEntity.builder();

        resourceTypeEntity.idResourceType( model.getIdResourceType() );
        resourceTypeEntity.name( model.getName() );
        resourceTypeEntity.isActive( model.getIsActive() );
        resourceTypeEntity.idCategoryResource( model.getIdCategoryResource() );
        resourceTypeEntity.categoryResource( categoryResourceToCategoryResourceEntity( model.getCategoryResource() ) );

        return resourceTypeEntity.build();
    }

    @Override
    public ResourceType toModel(ResourceTypeEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ResourceType.ResourceTypeBuilder resourceType = ResourceType.builder();

        resourceType.idResourceType( entity.getIdResourceType() );
        resourceType.name( entity.getName() );
        resourceType.isActive( entity.getIsActive() );
        resourceType.idCategoryResource( entity.getIdCategoryResource() );
        resourceType.categoryResource( categoryResourceEntityToCategoryResource( entity.getCategoryResource() ) );

        return resourceType.build();
    }

    @Override
    public List<ResourceType> toModelList(List<ResourceTypeEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ResourceType> list = new ArrayList<ResourceType>( entities.size() );
        for ( ResourceTypeEntity resourceTypeEntity : entities ) {
            list.add( toModel( resourceTypeEntity ) );
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
}
