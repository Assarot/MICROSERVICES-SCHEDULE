package pe.edu.upeu.microservice_inventory.infrastructure.adapter.in.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_inventory.domain.port.in.ResourceTypeUseCase;
import pe.edu.upeu.microservice_inventory.infrastructure.adapter.in.web.dto.ResourceTypeDto;
import pe.edu.upeu.microservice_inventory.infrastructure.adapter.in.web.mapper.WebMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/resource-types")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ResourceTypeController {
    
    private final ResourceTypeUseCase resourceTypeUseCase;
    private final WebMapper mapper;

    @PostMapping
    public ResponseEntity<ResourceTypeDto> createResourceType(@Valid @RequestBody ResourceTypeDto resourceTypeDto) {
        var resourceType = mapper.toResourceTypeDomain(resourceTypeDto);
        var createdResourceType = resourceTypeUseCase.createResourceType(resourceType);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResourceTypeDto(createdResourceType));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResourceTypeDto> updateResourceType(@PathVariable Long id, @Valid @RequestBody ResourceTypeDto resourceTypeDto) {
        var resourceType = mapper.toResourceTypeDomain(resourceTypeDto);
        var updatedResourceType = resourceTypeUseCase.updateResourceType(id, resourceType);
        return ResponseEntity.ok(mapper.toResourceTypeDto(updatedResourceType));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResourceType(@PathVariable Long id) {
        resourceTypeUseCase.deleteResourceType(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceTypeDto> getResourceTypeById(@PathVariable Long id) {
        return resourceTypeUseCase.getResourceTypeById(id)
                .map(resourceType -> ResponseEntity.ok(mapper.toResourceTypeDto(resourceType)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ResourceTypeDto>> getAllResourceTypes() {
        var resourceTypes = resourceTypeUseCase.getAllResourceTypes();
        var resourceTypeDtos = resourceTypes.stream()
                .map(mapper::toResourceTypeDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resourceTypeDtos);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ResourceTypeDto>> getActiveResourceTypes() {
        var resourceTypes = resourceTypeUseCase.getActiveResourceTypes();
        var resourceTypeDtos = resourceTypes.stream()
                .map(mapper::toResourceTypeDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resourceTypeDtos);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ResourceTypeDto>> getResourceTypesByCategoryId(@PathVariable Long categoryId) {
        var resourceTypes = resourceTypeUseCase.getResourceTypesByCategoryId(categoryId);
        var resourceTypeDtos = resourceTypes.stream()
                .map(mapper::toResourceTypeDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resourceTypeDtos);
    }
}
