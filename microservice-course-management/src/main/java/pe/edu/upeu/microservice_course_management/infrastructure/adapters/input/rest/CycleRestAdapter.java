package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_course_management.application.ports.input.CycleServicePort;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.mapper.CycleRestMapper;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request.CycleCreateRequest;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.respose.CycleResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cycle")
public class CycleRestAdapter {

    private final CycleServicePort servicePort;
    private final CycleRestMapper restMapper;

    @GetMapping("/v1/api")
    public List<CycleResponse> findAll() {
        return restMapper.toCycleResponseList(servicePort.findAll());
    }

    @GetMapping("/v1/api/{id}")
    public CycleResponse findById(@PathVariable Long id) {
        return restMapper.toCycleResponse(servicePort.findById(id));
    }

    @PostMapping("/v1/api")
    public ResponseEntity<CycleResponse> save(@Valid @RequestBody CycleCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toCycleResponse(
                        servicePort.save(restMapper.toCycle(request))));
    }

    @PutMapping("/v1/api/{id}")
    public CycleResponse update(@PathVariable Long id, @Valid @RequestBody CycleCreateRequest request) {
        return restMapper.toCycleResponse(servicePort.update(id, restMapper.toCycle(request)));
    }

    @DeleteMapping("/v1/api/{id}")
    public void delete(@PathVariable Long id) {
        servicePort.deleteById(id);
    }
}
