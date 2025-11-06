package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_course_management.application.ports.input.CourseServicePort;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.mapper.CourseRestMapper;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request.CourseCreateRequest;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.respose.CourseResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseRestAdapter {

    private final CourseServicePort servicePort;
    private final CourseRestMapper restMapper;

    @GetMapping("/v1/api")
    public List<CourseResponse> findAll() {
        return restMapper.toCoursesResposeList(servicePort.findAll());
    }

    @GetMapping("/v1/api/{id}")
    public CourseResponse findById(@PathVariable("id") Long id) {
        return restMapper.toCourseResponse(servicePort.findById(id));
    }

    @PostMapping("/v1/api")
    public ResponseEntity<CourseResponse> save(@Valid @RequestBody CourseCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toCourseResponse(
                        servicePort.save(restMapper.toCourse(request))));
    }

    @PutMapping("/v1/api/{id}")
    public CourseResponse update(@PathVariable("id") Long id, @Valid @RequestBody CourseCreateRequest request) {
        return restMapper.toCourseResponse(servicePort.update(id, restMapper.toCourse(request)));
    }

    @DeleteMapping("/v1/api/{id}")
    public void delete(@PathVariable("id") Long id) {
        servicePort.deleteById(id);
    }
}