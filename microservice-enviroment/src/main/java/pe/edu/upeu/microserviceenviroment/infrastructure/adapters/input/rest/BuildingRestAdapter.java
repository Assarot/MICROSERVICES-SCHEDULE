package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microserviceenviroment.application.ports.input.BuildingServicePort;
import pe.edu.upeu.microserviceenviroment.application.ports.input.FloorServicePort;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.mapper.BuildingRestMapper;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.mapper.FloorRestMapper;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.request.BuildingCreateRequest;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.response.BuildingResponse;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.response.FloorResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/building")
public class BuildingRestAdapter {

    private final BuildingServicePort servicePort;
    private final BuildingRestMapper restMapper;
    private final FloorServicePort floorServicePort;
    private final FloorRestMapper floorRestMapper;

    @GetMapping
    public List<BuildingResponse> findAll() {
        return restMapper.toBuildingResponseList(servicePort.findAll());
    }

    @GetMapping("/{id}")
    public BuildingResponse findById(@PathVariable("id") Long id) {
        return restMapper.toBuildingResponse(servicePort.findById(id));
    }

    @GetMapping("/{id}/floors")
    public List<FloorResponse> findFloorsByBuilding(@PathVariable("id") Long id) {
        return floorRestMapper.toFloorResponseList(floorServicePort.findByBuildingId(id));
    }

    @PostMapping
    public ResponseEntity<BuildingResponse> save(@Valid @RequestBody BuildingCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toBuildingResponse(
                        servicePort.save(restMapper.toBuilding(request))));
    }

    @PutMapping("/{id}")
    public BuildingResponse update(@PathVariable("id") Long id, @Valid @RequestBody BuildingCreateRequest request) {
        return restMapper.toBuildingResponse(servicePort.update(id, restMapper.toBuilding(request)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        servicePort.deleteById(id);
    }
}
