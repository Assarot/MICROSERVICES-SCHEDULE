package pe.edu.upeu.microservice_inventory.infrastructure.adapter.in.web.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_inventory.domain.model.*;
import pe.edu.upeu.microservice_inventory.infrastructure.adapter.in.web.dto.*;

@Component
public class WebMapper {
    
    // State mappings
    public State toStateDomain(StateDto dto) {
        if (dto == null) return null;
        return State.builder()
                .idState(dto.getIdState())
                .name(dto.getName())
                .isActive(dto.getIsActive())
                .build();
    }
    
    public StateDto toStateDto(State state) {
        if (state == null) return null;
        return StateDto.builder()
                .idState(state.getIdState())
                .name(state.getName())
                .isActive(state.getIsActive())
                .build();
    }
    
    // CategoryResource mappings
    public CategoryResource toCategoryResourceDomain(CategoryResourceDto dto) {
        if (dto == null) return null;
        return CategoryResource.builder()
                .idCategoryResource(dto.getIdCategoryResource())
                .name(dto.getName())
                .isActive(dto.getIsActive())
                .build();
    }
    
    public CategoryResourceDto toCategoryResourceDto(CategoryResource categoryResource) {
        if (categoryResource == null) return null;
        return CategoryResourceDto.builder()
                .idCategoryResource(categoryResource.getIdCategoryResource())
                .name(categoryResource.getName())
                .isActive(categoryResource.getIsActive())
                .build();
    }
    
    // ResourceType mappings
    public ResourceType toResourceTypeDomain(ResourceTypeDto dto) {
        if (dto == null) return null;
        return ResourceType.builder()
                .idResourceType(dto.getIdResourceType())
                .name(dto.getName())
                .isActive(dto.getIsActive())
                .idCategoryResource(dto.getIdCategoryResource())
                .build();
    }
    
    public ResourceTypeDto toResourceTypeDto(ResourceType resourceType) {
        if (resourceType == null) return null;
        return ResourceTypeDto.builder()
                .idResourceType(resourceType.getIdResourceType())
                .name(resourceType.getName())
                .isActive(resourceType.getIsActive())
                .idCategoryResource(resourceType.getIdCategoryResource())
                .categoryResource(resourceType.getCategoryResource() != null ? 
                    toCategoryResourceDto(resourceType.getCategoryResource()) : null)
                .build();
    }
    
    // Resource mappings
    public Resource toResourceDomain(ResourceDto dto) {
        if (dto == null) return null;
        return Resource.builder()
                .idResource(dto.getIdResource())
                .code(dto.getCode())
                .stock(dto.getStock())
                .resourcePhotoUrl(dto.getResourcePhotoUrl())
                .observation(dto.getObservation())
                .idResourceType(dto.getIdResourceType())
                .idState(dto.getIdState())
                .build();
    }
    
    public ResourceDto toResourceDto(Resource resource) {
        if (resource == null) return null;
        return ResourceDto.builder()
                .idResource(resource.getIdResource())
                .code(resource.getCode())
                .stock(resource.getStock())
                .resourcePhotoUrl(resource.getResourcePhotoUrl())
                .observation(resource.getObservation())
                .idResourceType(resource.getIdResourceType())
                .idState(resource.getIdState())
                .resourceType(resource.getResourceType() != null ? 
                    toResourceTypeDto(resource.getResourceType()) : null)
                .state(resource.getState() != null ? 
                    toStateDto(resource.getState()) : null)
                .build();
    }
    
    // ResourceAssignment mappings
    public ResourceAssignment toResourceAssignmentDomain(ResourceAssignmentDto dto) {
        if (dto == null) return null;
        return ResourceAssignment.builder()
                .idResourceAssignment(dto.getIdResourceAssignment())
                .idAcademicSpace(dto.getIdAcademicSpace())
                .idResource(dto.getIdResource())
                .build();
    }
    
    public ResourceAssignmentDto toResourceAssignmentDto(ResourceAssignment resourceAssignment) {
        if (resourceAssignment == null) return null;
        return ResourceAssignmentDto.builder()
                .idResourceAssignment(resourceAssignment.getIdResourceAssignment())
                .idAcademicSpace(resourceAssignment.getIdAcademicSpace())
                .idResource(resourceAssignment.getIdResource())
                .resource(resourceAssignment.getResource() != null ? 
                    toResourceDto(resourceAssignment.getResource()) : null)
                .build();
    }
}
