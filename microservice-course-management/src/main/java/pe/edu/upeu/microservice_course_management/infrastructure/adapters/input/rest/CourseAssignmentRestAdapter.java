package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_course_management.application.ports.input.CourseAssignmentServicePort;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.mapper.CourseAssignmentRestMapper;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request.CourseAssignmentCreateRequest;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.respose.CourseAssignmentResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course-assignment")
public class CourseAssignmentRestAdapter {

    private final CourseAssignmentServicePort servicePort;
    private final CourseAssignmentRestMapper restMapper;

    @GetMapping("/v1/api")
    public List<CourseAssignmentResponse> findAll() {
        return restMapper.toCourseAssignmentResponseList(servicePort.findAll());
    }

    @GetMapping("/v1/api/{id}")
    public CourseAssignmentResponse findById(@PathVariable Long id) {
        return restMapper.toCourseAssignmentResponse(servicePort.findById(id));
    }

    @PostMapping("/v1/api")
    public ResponseEntity<CourseAssignmentResponse> save(@Valid @RequestBody CourseAssignmentCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toCourseAssignmentResponse(
                        servicePort.save(restMapper.toCourseAssignment(request))));
    }

    @PutMapping("/v1/api/{id}")
    public CourseAssignmentResponse update(@PathVariable Long id, @Valid @RequestBody CourseAssignmentCreateRequest request) {
        return restMapper.toCourseAssignmentResponse(servicePort.update(id, restMapper.toCourseAssignment(request)));
    }

    @DeleteMapping("/v1/api/{id}")
    public void delete(@PathVariable Long id) {
        servicePort.deleteById(id);
    }

}
