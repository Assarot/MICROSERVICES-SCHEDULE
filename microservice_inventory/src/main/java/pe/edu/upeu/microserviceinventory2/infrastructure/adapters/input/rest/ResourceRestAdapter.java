package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.microserviceinventory2.application.ports.input.ResourceServicePort;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.mapper.ResourceRestMapper;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.request.ResourceRequest;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.response.ResourceResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resources")
public class ResourceRestAdapter {

    private final ResourceServicePort servicePort;
    private final ResourceRestMapper restMapper;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ResourceResponse> create(
            @Valid @RequestPart("resource") ResourceRequest request,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {
        var created = servicePort.save(restMapper.toModel(request), photo);
        return ResponseEntity.status(HttpStatus.CREATED).body(restMapper.toResponse(created));
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResourceResponse update(
            @PathVariable("id") Long id,
            @Valid @RequestPart("resource") ResourceRequest request,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {
        return restMapper.toResponse(servicePort.update(id, restMapper.toModel(request), photo));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        servicePort.deleteById(id);
    }

    @GetMapping("/{id}")
    public ResourceResponse getById(@PathVariable("id") Long id) {
        return restMapper.toResponse(servicePort.findById(id));
    }

    @GetMapping
    public List<ResourceResponse> getAll() {
        return restMapper.toResponseList(servicePort.findAll());
    }

    @GetMapping("/type/{typeId}")
    public List<ResourceResponse> getByType(@PathVariable("typeId") Long typeId) {
        return restMapper.toResponseList(servicePort.findByTypeId(typeId));
    }

    @GetMapping("/state/{stateId}")
    public List<ResourceResponse> getByState(@PathVariable("stateId") Long stateId) {
        return restMapper.toResponseList(servicePort.findByStateId(stateId));
    }
}
