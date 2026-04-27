package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microserviceinventory2.application.ports.input.StateServicePort;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.mapper.StateRestMapper;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.request.StateCreateRequest;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.response.StateResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/states")
public class StateRestAdapter {

    private final StateServicePort servicePort;
    private final StateRestMapper restMapper;

    @GetMapping
    public List<StateResponse> findAll() {
        return restMapper.toStateResponseList(servicePort.findAll());
    }

    @GetMapping("/{id}")
    public StateResponse findById(@PathVariable("id") Long id) {
        return restMapper.toStateResponse(servicePort.findById(id));
    }

    @PostMapping
    public ResponseEntity<StateResponse> create(@Valid @RequestBody StateCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toStateResponse(servicePort.save(restMapper.toState(request))));
    }

    @PutMapping("/{id}")
    public StateResponse update(@PathVariable("id") Long id, @Valid @RequestBody StateCreateRequest request) {
        return restMapper.toStateResponse(servicePort.update(id, restMapper.toState(request)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        servicePort.deleteById(id);
    }
}
