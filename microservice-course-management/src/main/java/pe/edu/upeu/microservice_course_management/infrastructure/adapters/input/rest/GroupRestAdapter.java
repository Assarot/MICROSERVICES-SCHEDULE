package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_course_management.application.ports.input.GroupServicePort;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.mapper.GroupRestMapper;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request.GroupCreateRequest;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.respose.GroupResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/group")
public class GroupRestAdapter {
    private final GroupServicePort servicePort;
    private final GroupRestMapper restMapper;

    @GetMapping("/v1/api")
    public List<GroupResponse> findAll() {
        return restMapper.toGroupResponseList(servicePort.findAll());
    }

    @GetMapping("/v1/api/{id}")
    public GroupResponse findById(@PathVariable Long id) {
        return restMapper.toGroupResponse(servicePort.findById(id));
    }

    @PostMapping("/v1/api")
    public ResponseEntity<GroupResponse> save(@Valid @RequestBody GroupCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toGroupResponse(
                        servicePort.save(restMapper.toGroup(request))));
    }

    @PutMapping("/v1/api/{id}")
    public GroupResponse update(@PathVariable Long id, @Valid @RequestBody GroupCreateRequest request) {
        return restMapper.toGroupResponse(servicePort.update(id, restMapper.toGroup(request)));
    }

    @DeleteMapping("/v1/api/{id}")
    public void delete(@PathVariable Long id) {
        servicePort.deleteById(id);
    }
}
