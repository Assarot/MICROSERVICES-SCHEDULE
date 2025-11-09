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
    date = "2025-11-06T09:06:19-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251023-0518, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class ResourceTypePersistenceMapperImpl implements ResourceTypePersistenceMapper {

    @Override
    public ResourceTypeEntity toEntity(ResourceType model) {
        if ( model == null ) {
            return null;
        }

        ResourceTypeEntity.ResourceTypeEntityBuilder resourceTypeEntity = ResourceTypeEntity.builder();

        resourceTypeEntity.categoryResource( categoryResourceToCategoryResourceEntity( model.getCategoryResource() ) );
        resourceTypeEntity.idCategoryResource( model.getIdCategoryResource() );
        resourceTypeEntity.idResourceType( model.getIdResourceType() );
        resourceTypeEntity.isActive( model.getIsActive() );
        resourceTypeEntity.name( model.getName() );

        return resourceTypeEntity.build();
    }

    @Override
    public ResourceType toModel(ResourceTypeEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ResourceType.ResourceTypeBuilder resourceType = ResourceType.builder();

        resourceType.categoryResource( categoryResourceEntityToCategoryResource( entity.getCategoryResource() ) );
        resourceType.idCategoryResource( entity.getIdCategoryResource() );
        resourceType.idResourceType( entity.getIdResourceType() );
        resourceType.isActive( entity.getIsActive() );
        resourceType.name( entity.getName() );

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
        categoryResourceEntity.isActive( categoryResource.getIsActive() );
        categoryResourceEntity.name( categoryResource.getName() );

        return categoryResourceEntity.build();
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
}
