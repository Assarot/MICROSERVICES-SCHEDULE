package pe.edu.upeu.microservice_inventory.infrastructure.adapter.in.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.microservice_inventory.domain.port.in.ResourceUseCase;
import pe.edu.upeu.microservice_inventory.infrastructure.adapter.in.web.dto.ResourceDto;
import pe.edu.upeu.microservice_inventory.infrastructure.adapter.in.web.mapper.WebMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ResourceController {
    
    private final ResourceUseCase resourceUseCase;
    private final WebMapper mapper;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResourceDto> createResource(
            @RequestPart("resource") String resourceJson,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {
        try {
            ResourceDto resourceDto = objectMapper.readValue(resourceJson, ResourceDto.class);
            var resource = mapper.toResourceDomain(resourceDto);
            var createdResource = resourceUseCase.createResource(resource, photo);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResourceDto(createdResource));
        } catch (Exception e) {
            throw new RuntimeException("Error creating resource: " + e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResourceDto> updateResource(
            @PathVariable Long id,
            @RequestPart("resource") String resourceJson,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {
        try {
            ResourceDto resourceDto = objectMapper.readValue(resourceJson, ResourceDto.class);
            var resource = mapper.toResourceDomain(resourceDto);
            var updatedResource = resourceUseCase.updateResource(id, resource, photo);
            return ResponseEntity.ok(mapper.toResourceDto(updatedResource));
        } catch (Exception e) {
            throw new RuntimeException("Error updating resource: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        resourceUseCase.deleteResource(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceDto> getResourceById(@PathVariable Long id) {
        return resourceUseCase.getResourceById(id)
                .map(resource -> ResponseEntity.ok(mapper.toResourceDto(resource)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ResourceDto>> getAllResources() {
        var resources = resourceUseCase.getAllResources();
        var resourceDtos = resources.stream()
                .map(mapper::toResourceDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resourceDtos);
    }

    @GetMapping("/type/{typeId}")
    public ResponseEntity<List<ResourceDto>> getResourcesByTypeId(@PathVariable Long typeId) {
        var resources = resourceUseCase.getResourcesByTypeId(typeId);
        var resourceDtos = resources.stream()
                .map(mapper::toResourceDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resourceDtos);
    }

    @GetMapping("/state/{stateId}")
    public ResponseEntity<List<ResourceDto>> getResourcesByStateId(@PathVariable Long stateId) {
        var resources = resourceUseCase.getResourcesByStateId(stateId);
        var resourceDtos = resources.stream()
                .map(mapper::toResourceDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resourceDtos);
    }
}
