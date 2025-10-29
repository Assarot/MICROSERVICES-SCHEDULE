package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microserviceinventory2.application.ports.input.ResourceTypeServicePort;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.mapper.ResourceTypeRestMapper;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.request.ResourceTypeRequest;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.response.ResourceTypeResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resource-types")
public class ResourceTypeRestAdapter {

    private final ResourceTypeServicePort servicePort;
    private final ResourceTypeRestMapper restMapper;

    @PostMapping
    public ResponseEntity<ResourceTypeResponse> create(@Valid @RequestBody ResourceTypeRequest request) {
        var created = servicePort.save(restMapper.toModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(restMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public ResourceTypeResponse update(@PathVariable Long id, @Valid @RequestBody ResourceTypeRequest request) {
        return restMapper.toResponse(servicePort.update(id, restMapper.toModel(request)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        servicePort.deleteById(id);
    }

    @GetMapping("/{id}")
    public ResourceTypeResponse getById(@PathVariable Long id) {
        return restMapper.toResponse(servicePort.findById(id));
    }

    @GetMapping
    public List<ResourceTypeResponse> getAll() {
        return restMapper.toResponseList(servicePort.findAll());
    }

    @GetMapping("/active")
    public List<ResourceTypeResponse> getActive() {
        return restMapper.toResponseList(servicePort.findActive());
    }

    @GetMapping("/category/{categoryId}")
    public List<ResourceTypeResponse> getByCategory(@PathVariable Long categoryId) {
        return restMapper.toResponseList(servicePort.findByCategoryId(categoryId));
    }
}
