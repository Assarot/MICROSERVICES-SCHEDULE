package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microserviceinventory2.application.ports.input.CategoryResourceServicePort;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.mapper.CategoryResourceRestMapper;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.request.CategoryResourceRequest;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.response.CategoryResourceResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category-resources")
public class CategoryResourceRestAdapter {

    private final CategoryResourceServicePort servicePort;
    private final CategoryResourceRestMapper restMapper;

    @PostMapping
    public ResponseEntity<CategoryResourceResponse> create(@Valid @RequestBody CategoryResourceRequest request) {
        var created = servicePort.save(restMapper.toModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(restMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public CategoryResourceResponse update(@PathVariable("id") Long id, @Valid @RequestBody CategoryResourceRequest request) {
        return restMapper.toResponse(servicePort.update(id, restMapper.toModel(request)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        servicePort.deleteById(id);
    }

    @GetMapping("/{id}")
    public CategoryResourceResponse getById(@PathVariable("id") Long id) {
        return restMapper.toResponse(servicePort.findById(id));
    }

    @GetMapping
    public List<CategoryResourceResponse> getAll() {
        return restMapper.toResponseList(servicePort.findAll());
    }

    @GetMapping("/active")
    public List<CategoryResourceResponse> getActive() {
        return restMapper.toResponseList(servicePort.findActive());
    }
}
