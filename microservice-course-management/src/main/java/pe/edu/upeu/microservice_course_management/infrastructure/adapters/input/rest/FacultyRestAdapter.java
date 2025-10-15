package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_course_management.application.ports.input.FacultyServicePort;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.mapper.FacultyRestMapper;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request.FacultyCreateRequest;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.respose.FacultyResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/faculty")
public class FacultyRestAdapter {

    private final FacultyServicePort servicePort;
    private final FacultyRestMapper restMapper;

    @GetMapping("/v1/api")
    public List<FacultyResponse> findAll() {
        return restMapper.toFacultyResponseList(servicePort.findAll());
    }

    @GetMapping("/v1/api/{id}")
    public FacultyResponse findById(@PathVariable Long id) {
        return restMapper.toFacultyResponse(servicePort.findById(id));
    }

    @PostMapping("/v1/api")
    public ResponseEntity<FacultyResponse> save(@Valid @RequestBody FacultyCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toFacultyResponse(
                        servicePort.save(restMapper.toFaculty(request))));
    }

    @PutMapping("/v1/api/{id}")
    public FacultyResponse update(@PathVariable Long id, @Valid @RequestBody FacultyCreateRequest request) {
        return restMapper.toFacultyResponse(servicePort.update(id, restMapper.toFaculty(request)));
    }

    @DeleteMapping("/v1/api/{id}")
    public void delete(@PathVariable Long id) {
        servicePort.deleteById(id);
    }

}
