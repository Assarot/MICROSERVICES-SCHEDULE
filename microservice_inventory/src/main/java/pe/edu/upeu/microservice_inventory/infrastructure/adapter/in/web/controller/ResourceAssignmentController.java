package pe.edu.upeu.microservice_inventory.infrastructure.adapter.in.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_inventory.domain.port.in.ResourceAssignmentUseCase;
import pe.edu.upeu.microservice_inventory.infrastructure.adapter.in.web.dto.ResourceAssignmentDto;
import pe.edu.upeu.microservice_inventory.infrastructure.adapter.in.web.mapper.WebMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/resource-assignments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ResourceAssignmentController {
    
    private final ResourceAssignmentUseCase resourceAssignmentUseCase;
    private final WebMapper mapper;

    @PostMapping
    public ResponseEntity<ResourceAssignmentDto> createResourceAssignment(@Valid @RequestBody ResourceAssignmentDto resourceAssignmentDto) {
        var resourceAssignment = mapper.toResourceAssignmentDomain(resourceAssignmentDto);
        var createdResourceAssignment = resourceAssignmentUseCase.createResourceAssignment(resourceAssignment);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResourceAssignmentDto(createdResourceAssignment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResourceAssignmentDto> updateResourceAssignment(@PathVariable Long id, @Valid @RequestBody ResourceAssignmentDto resourceAssignmentDto) {
        var resourceAssignment = mapper.toResourceAssignmentDomain(resourceAssignmentDto);
        var updatedResourceAssignment = resourceAssignmentUseCase.updateResourceAssignment(id, resourceAssignment);
        return ResponseEntity.ok(mapper.toResourceAssignmentDto(updatedResourceAssignment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResourceAssignment(@PathVariable Long id) {
        resourceAssignmentUseCase.deleteResourceAssignment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceAssignmentDto> getResourceAssignmentById(@PathVariable Long id) {
        return resourceAssignmentUseCase.getResourceAssignmentById(id)
                .map(resourceAssignment -> ResponseEntity.ok(mapper.toResourceAssignmentDto(resourceAssignment)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ResourceAssignmentDto>> getAllResourceAssignments() {
        var resourceAssignments = resourceAssignmentUseCase.getAllResourceAssignments();
        var resourceAssignmentDtos = resourceAssignments.stream()
                .map(mapper::toResourceAssignmentDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resourceAssignmentDtos);
    }

    @GetMapping("/resource/{resourceId}")
    public ResponseEntity<List<ResourceAssignmentDto>> getResourceAssignmentsByResourceId(@PathVariable Long resourceId) {
        var resourceAssignments = resourceAssignmentUseCase.getResourceAssignmentsByResourceId(resourceId);
        var resourceAssignmentDtos = resourceAssignments.stream()
                .map(mapper::toResourceAssignmentDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resourceAssignmentDtos);
    }

    @GetMapping("/academic-space/{academicSpaceId}")
    public ResponseEntity<List<ResourceAssignmentDto>> getResourceAssignmentsByAcademicSpaceId(@PathVariable Long academicSpaceId) {
        var resourceAssignments = resourceAssignmentUseCase.getResourceAssignmentsByAcademicSpaceId(academicSpaceId);
        var resourceAssignmentDtos = resourceAssignments.stream()
                .map(mapper::toResourceAssignmentDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resourceAssignmentDtos);
    }
}
