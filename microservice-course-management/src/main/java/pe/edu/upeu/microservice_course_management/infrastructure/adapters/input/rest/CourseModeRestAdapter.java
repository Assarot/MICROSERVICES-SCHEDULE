package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_course_management.application.ports.input.CourseModeServicePort;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.mapper.CourseModeRestMapper;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request.CourseModeCreateRequest;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.respose.CourseModeResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course-mode")
public class CourseModeRestAdapter {

    private final CourseModeServicePort servicePort;
    private final CourseModeRestMapper restMapper;

    @GetMapping("/v1/api")
    public List<CourseModeResponse> findAll() {
        return restMapper.toCourseModeResponseList(servicePort.findAll());
    }

    @GetMapping("/v1/api/{id}")
    public CourseModeResponse findById(@PathVariable Long id) {
        return restMapper.toCourseModeResponse(servicePort.findById(id));
    }

    @PostMapping("/v1/api")
    public ResponseEntity<CourseModeResponse> save(@Valid @RequestBody CourseModeCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toCourseModeResponse(
                        servicePort.save(restMapper.toCourseMode(request))));
    }

    @PutMapping("/v1/api/{id}")
    public CourseModeResponse update(@PathVariable Long id, @Valid @RequestBody CourseModeCreateRequest request) {
        return restMapper.toCourseModeResponse(servicePort.update(id, restMapper.toCourseMode(request)));
    }

    @DeleteMapping("/v1/api/{id}")
    public void delete(@PathVariable Long id) {
        servicePort.deleteById(id);
    }
}
