package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pe.edu.upeu.microserviceinventory2.domain.model.ResourceType;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.ResourceTypeEntity;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResourceTypePersistenceMapper {
    ResourceTypeEntity toEntity(ResourceType model);
    ResourceType toModel(ResourceTypeEntity entity);
    List<ResourceType> toModelList(List<ResourceTypeEntity> entities);
}
