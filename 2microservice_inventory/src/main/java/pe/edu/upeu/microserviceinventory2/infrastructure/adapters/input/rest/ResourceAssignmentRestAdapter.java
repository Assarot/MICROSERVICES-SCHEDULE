package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microserviceinventory2.application.ports.input.ResourceAssignmentServicePort;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.mapper.ResourceAssignmentRestMapper;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.request.ResourceAssignmentRequest;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.response.ResourceAssignmentResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resource-assignments")
public class ResourceAssignmentRestAdapter {

    private final ResourceAssignmentServicePort servicePort;
    private final ResourceAssignmentRestMapper restMapper;

    @PostMapping
    public ResponseEntity<ResourceAssignmentResponse> create(@Valid @RequestBody ResourceAssignmentRequest request) {
        var created = servicePort.save(restMapper.toModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(restMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public ResourceAssignmentResponse update(@PathVariable("id") Long id, @Valid @RequestBody ResourceAssignmentRequest request) {
        return restMapper.toResponse(servicePort.update(id, restMapper.toModel(request)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        servicePort.deleteById(id);
    }

    @GetMapping("/{id}")
    public ResourceAssignmentResponse getById(@PathVariable("id") Long id) {
        return restMapper.toResponse(servicePort.findById(id));
    }

    @GetMapping
    public List<ResourceAssignmentResponse> getAll() {
        return restMapper.toResponseList(servicePort.findAll());
    }

    @GetMapping("/resource/{resourceId}")
    public List<ResourceAssignmentResponse> getByResource(@PathVariable("resourceId") Long resourceId) {
        return restMapper.toResponseList(servicePort.findByResourceId(resourceId));
    }

    @GetMapping("/academic-space/{academicSpaceId}")
    public List<ResourceAssignmentResponse> getByAcademicSpace(@PathVariable("academicSpaceId") Long academicSpaceId) {
        return restMapper.toResponseList(servicePort.findByAcademicSpaceId(academicSpaceId));
    }
}
