package pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_inventory.domain.model.*;
import pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence.entity.*;

@Component
public class PersistenceMapper {
    
    // State mappings
    public StateEntity toStateEntity(State state) {
        if (state == null) return null;
        return StateEntity.builder()
                .idState(state.getIdState())
                .name(state.getName())
                .isActive(state.getIsActive())
                .build();
    }
    
    public State toStateDomain(StateEntity entity) {
        if (entity == null) return null;
        return State.builder()
                .idState(entity.getIdState())
                .name(entity.getName())
                .isActive(entity.getIsActive())
                .build();
    }
    
    // CategoryResource mappings
    public CategoryResourceEntity toCategoryResourceEntity(CategoryResource categoryResource) {
        if (categoryResource == null) return null;
        return CategoryResourceEntity.builder()
                .idCategoryResource(categoryResource.getIdCategoryResource())
                .name(categoryResource.getName())
                .isActive(categoryResource.getIsActive())
                .build();
    }
    
    public CategoryResource toCategoryResourceDomain(CategoryResourceEntity entity) {
        if (entity == null) return null;
        return CategoryResource.builder()
                .idCategoryResource(entity.getIdCategoryResource())
                .name(entity.getName())
                .isActive(entity.getIsActive())
                .build();
    }
    
    // ResourceType mappings
    public ResourceTypeEntity toResourceTypeEntity(ResourceType resourceType) {
        if (resourceType == null) return null;
        return ResourceTypeEntity.builder()
                .idResourceType(resourceType.getIdResourceType())
                .name(resourceType.getName())
                .isActive(resourceType.getIsActive())
                .idCategoryResource(resourceType.getIdCategoryResource())
                .build();
    }
    
    public ResourceType toResourceTypeDomain(ResourceTypeEntity entity) {
        if (entity == null) return null;
        return ResourceType.builder()
                .idResourceType(entity.getIdResourceType())
                .name(entity.getName())
                .isActive(entity.getIsActive())
                .idCategoryResource(entity.getIdCategoryResource())
                .categoryResource(entity.getCategoryResource() != null ? 
                    toCategoryResourceDomain(entity.getCategoryResource()) : null)
                .build();
    }
    
    // Resource mappings
    public ResourceEntity toResourceEntity(Resource resource) {
        if (resource == null) return null;
        return ResourceEntity.builder()
                .idResource(resource.getIdResource())
                .code(resource.getCode())
                .stock(resource.getStock())
                .resourcePhotoUrl(resource.getResourcePhotoUrl())
                .observation(resource.getObservation())
                .idResourceType(resource.getIdResourceType())
                .idState(resource.getIdState())
                .build();
    }
    
    public Resource toResourceDomain(ResourceEntity entity) {
        if (entity == null) return null;
        return Resource.builder()
                .idResource(entity.getIdResource())
                .code(entity.getCode())
                .stock(entity.getStock())
                .resourcePhotoUrl(entity.getResourcePhotoUrl())
                .observation(entity.getObservation())
                .idResourceType(entity.getIdResourceType())
                .idState(entity.getIdState())
                .resourceType(entity.getResourceType() != null ? 
                    toResourceTypeDomain(entity.getResourceType()) : null)
                .state(entity.getState() != null ? 
                    toStateDomain(entity.getState()) : null)
                .build();
    }
    
    // ResourceAssignment mappings
    public ResourceAssignmentEntity toResourceAssignmentEntity(ResourceAssignment resourceAssignment) {
        if (resourceAssignment == null) return null;
        return ResourceAssignmentEntity.builder()
                .idResourceAssignment(resourceAssignment.getIdResourceAssignment())
                .idAcademicSpace(resourceAssignment.getIdAcademicSpace())
                .idResource(resourceAssignment.getIdResource())
                .build();
    }
    
    public ResourceAssignment toResourceAssignmentDomain(ResourceAssignmentEntity entity) {
        if (entity == null) return null;
        return ResourceAssignment.builder()
                .idResourceAssignment(entity.getIdResourceAssignment())
                .idAcademicSpace(entity.getIdAcademicSpace())
                .idResource(entity.getIdResource())
                .resource(entity.getResource() != null ? 
                    toResourceDomain(entity.getResource()) : null)
                .build();
    }
}
