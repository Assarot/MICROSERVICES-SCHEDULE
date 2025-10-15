package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_course_management.application.ports.input.PlanServicePort;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.mapper.PlanRestMapper;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request.PlanCreateRequest;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.respose.PlanResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plan")
public class PlanRestAdapter {

    private final PlanServicePort servicePort;
    private final PlanRestMapper restMapper;

    @GetMapping("/v1/api")
    public List<PlanResponse> findAll(){
        return restMapper.toPlanResposeList(servicePort.findAll());
    }

    @GetMapping("/v1/api/{id}")
    public PlanResponse findById(@PathVariable Long id){
        return restMapper.toPlanResponse(servicePort.findById(id));
    }

    @PostMapping("/v1/api")
    public ResponseEntity<PlanResponse> save(@Valid @RequestBody PlanCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toPlanResponse(
                        servicePort.save(restMapper.toPlan(request))));
    }

    @PutMapping("/v1/api/{id}")
    public PlanResponse update(@PathVariable Long id, @Valid @RequestBody PlanCreateRequest request) {
        return restMapper.toPlanResponse(servicePort.update(id, restMapper.toPlan(request)));
    }

    @DeleteMapping("/v1/api/{id}")
    public void delete(@PathVariable Long id) {
        servicePort.deleteById(id);
    }
}
