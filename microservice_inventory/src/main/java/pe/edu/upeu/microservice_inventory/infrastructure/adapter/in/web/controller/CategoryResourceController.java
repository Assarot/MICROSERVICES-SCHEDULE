package pe.edu.upeu.microservice_inventory.infrastructure.adapter.in.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_inventory.domain.port.in.CategoryResourceUseCase;
import pe.edu.upeu.microservice_inventory.infrastructure.adapter.in.web.dto.CategoryResourceDto;
import pe.edu.upeu.microservice_inventory.infrastructure.adapter.in.web.mapper.WebMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category-resources")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoryResourceController {
    
    private final CategoryResourceUseCase categoryResourceUseCase;
    private final WebMapper mapper;

    @PostMapping
    public ResponseEntity<CategoryResourceDto> createCategoryResource(@Valid @RequestBody CategoryResourceDto categoryResourceDto) {
        var categoryResource = mapper.toCategoryResourceDomain(categoryResourceDto);
        var createdCategoryResource = categoryResourceUseCase.createCategoryResource(categoryResource);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toCategoryResourceDto(createdCategoryResource));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResourceDto> updateCategoryResource(@PathVariable Long id, @Valid @RequestBody CategoryResourceDto categoryResourceDto) {
        var categoryResource = mapper.toCategoryResourceDomain(categoryResourceDto);
        var updatedCategoryResource = categoryResourceUseCase.updateCategoryResource(id, categoryResource);
        return ResponseEntity.ok(mapper.toCategoryResourceDto(updatedCategoryResource));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryResource(@PathVariable Long id) {
        categoryResourceUseCase.deleteCategoryResource(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResourceDto> getCategoryResourceById(@PathVariable Long id) {
        return categoryResourceUseCase.getCategoryResourceById(id)
                .map(categoryResource -> ResponseEntity.ok(mapper.toCategoryResourceDto(categoryResource)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<CategoryResourceDto>> getAllCategoryResources() {
        var categoryResources = categoryResourceUseCase.getAllCategoryResources();
        var categoryResourceDtos = categoryResources.stream()
                .map(mapper::toCategoryResourceDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryResourceDtos);
    }

    @GetMapping("/active")
    public ResponseEntity<List<CategoryResourceDto>> getActiveCategoryResources() {
        var categoryResources = categoryResourceUseCase.getActiveCategoryResources();
        var categoryResourceDtos = categoryResources.stream()
                .map(mapper::toCategoryResourceDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryResourceDtos);
    }
}
