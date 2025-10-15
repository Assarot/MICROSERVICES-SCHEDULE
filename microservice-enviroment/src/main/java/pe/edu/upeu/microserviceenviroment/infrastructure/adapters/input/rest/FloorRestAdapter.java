package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microserviceenviroment.application.ports.input.FloorServicePort;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.mapper.FloorRestMapper;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.request.FloorCreateRequest;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.response.FloorResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/floor")
public class FloorRestAdapter {


    private final FloorServicePort servicePort;
    private final FloorRestMapper restMapper;

    @GetMapping
    public List<FloorResponse> findAll() {
        return restMapper.toFloorResponseList(servicePort.findAll());
    }

    @GetMapping("/{id}")
    public FloorResponse findById(@PathVariable("id") Long id) {
        return restMapper.toFloorResponse(servicePort.findById(id));
    }

    @PostMapping
    public ResponseEntity<FloorResponse> create(@Valid @RequestBody FloorCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toFloorResponse(
                        servicePort.save(restMapper.toFloor(request))));
    }

    @PutMapping("/{id}")
    public FloorResponse update(@PathVariable("id") Long id, @Valid @RequestBody FloorCreateRequest request) {
        return restMapper.toFloorResponse(servicePort.update(id, restMapper.toFloor(request)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        servicePort.deleteById(id);
    }
}
