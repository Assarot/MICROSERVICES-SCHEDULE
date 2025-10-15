package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microserviceenviroment.application.ports.input.TypeAcademicSpaceServicePort;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.mapper.TypeAcademicSpaceRestMapper;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.request.TypeAcademicSpaceCreateRequest;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.response.TypeAcademicSpaceResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/type-academic-space")
public class TypeAcademicSpaceAdapter {

    private final TypeAcademicSpaceServicePort servicePort;
    private final TypeAcademicSpaceRestMapper restMapper;

    @GetMapping
    public List<TypeAcademicSpaceResponse> findAll() {
        return restMapper.toTypeAcademicSpaceResponsesList(servicePort.findAll());
    }

    @GetMapping("/{id}")
    public TypeAcademicSpaceResponse findById(@PathVariable("id") Long id) {
        return restMapper.toTypeAcademicSpaceResponse(servicePort.findById(id));
    }

    @PostMapping
    public ResponseEntity<TypeAcademicSpaceResponse> create(@RequestBody TypeAcademicSpaceCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toTypeAcademicSpaceResponse(
                        servicePort.save(restMapper.toTypeAcademicSpace(request))));
    }

    @PutMapping("/{id}")
    public TypeAcademicSpaceResponse update(@PathVariable("id") Long id, @Valid @RequestBody TypeAcademicSpaceCreateRequest request) {
        return restMapper.toTypeAcademicSpaceResponse(servicePort.update(id, restMapper.toTypeAcademicSpace(request)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        servicePort.deleteById(id);
    }
}
