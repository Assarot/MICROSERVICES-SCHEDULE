package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microserviceinventory2.domain.model.CategoryResource;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.CategoryResourceEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-29T13:34:13-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class CategoryResourcePersistenceMapperImpl implements CategoryResourcePersistenceMapper {

    @Override
    public CategoryResourceEntity toEntity(CategoryResource model) {
        if ( model == null ) {
            return null;
        }

        CategoryResourceEntity.CategoryResourceEntityBuilder categoryResourceEntity = CategoryResourceEntity.builder();

        categoryResourceEntity.idCategoryResource( model.getIdCategoryResource() );
        categoryResourceEntity.name( model.getName() );
        categoryResourceEntity.isActive( model.getIsActive() );

        return categoryResourceEntity.build();
    }

    @Override
    public CategoryResource toModel(CategoryResourceEntity entity) {
        if ( entity == null ) {
            return null;
        }

        CategoryResource.CategoryResourceBuilder categoryResource = CategoryResource.builder();

        categoryResource.idCategoryResource( entity.getIdCategoryResource() );
        categoryResource.name( entity.getName() );
        categoryResource.isActive( entity.getIsActive() );

        return categoryResource.build();
    }

    @Override
    public List<CategoryResource> toModelList(List<CategoryResourceEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<CategoryResource> list = new ArrayList<CategoryResource>( entities.size() );
        for ( CategoryResourceEntity categoryResourceEntity : entities ) {
            list.add( toModel( categoryResourceEntity ) );
        }

        return list;
    }
}
