package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microserviceenviroment.application.ports.input.AcademicSpaceServicePort;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.mapper.AcademicSpaceRestMapper;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.request.AcademicSpaceCreateRequest;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.response.AcademicSpaceResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/academic-space")
public class AcademicSpaceAdapter {

    private final AcademicSpaceServicePort servicePort;
    private final AcademicSpaceRestMapper restMapper;

    @GetMapping
    public List<AcademicSpaceResponse> findAll() {
        return restMapper.toAcademicSpaceResponseList(servicePort.findAll());
    }

    @GetMapping("/{id}")
    public AcademicSpaceResponse findById(@PathVariable("id") Long id) {
        return restMapper.toAcademicSpaceResponse(servicePort.findById(id));
    }

    @PostMapping
    public ResponseEntity<AcademicSpaceResponse> save(@Valid @RequestBody AcademicSpaceCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toAcademicSpaceResponse(
                        servicePort.save(restMapper.toAcademicSpace(request))));
    }

    @PutMapping("/{id}")
    public AcademicSpaceResponse update(@PathVariable("id") Long id, @Valid @RequestBody AcademicSpaceCreateRequest request) {
        return restMapper.toAcademicSpaceResponse(servicePort.update(id, restMapper.toAcademicSpace(request)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        servicePort.deleteById(id);
    }
}
