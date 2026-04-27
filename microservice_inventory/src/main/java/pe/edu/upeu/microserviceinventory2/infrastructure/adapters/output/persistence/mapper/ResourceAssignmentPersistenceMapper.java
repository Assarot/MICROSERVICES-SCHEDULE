package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pe.edu.upeu.microserviceinventory2.domain.model.ResourceAssignment;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.ResourceAssignmentEntity;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResourceAssignmentPersistenceMapper {
    ResourceAssignmentEntity toEntity(ResourceAssignment model);
    ResourceAssignment toModel(ResourceAssignmentEntity entity);
    List<ResourceAssignment> toModelList(List<ResourceAssignmentEntity> entities);
}
