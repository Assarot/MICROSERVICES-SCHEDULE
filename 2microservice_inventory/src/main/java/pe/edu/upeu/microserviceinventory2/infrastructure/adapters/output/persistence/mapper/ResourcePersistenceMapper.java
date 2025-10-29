package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pe.edu.upeu.microserviceinventory2.domain.model.Resource;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.ResourceEntity;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResourcePersistenceMapper {
    ResourceEntity toEntity(Resource model);
    Resource toModel(ResourceEntity entity);
    List<Resource> toModelList(List<ResourceEntity> entities);
}
