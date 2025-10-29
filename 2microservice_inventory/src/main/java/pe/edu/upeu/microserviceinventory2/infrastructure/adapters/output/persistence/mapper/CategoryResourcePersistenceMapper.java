package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pe.edu.upeu.microserviceinventory2.domain.model.CategoryResource;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.CategoryResourceEntity;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryResourcePersistenceMapper {
    CategoryResourceEntity toEntity(CategoryResource model);
    CategoryResource toModel(CategoryResourceEntity entity);
    List<CategoryResource> toModelList(List<CategoryResourceEntity> entities);
}
