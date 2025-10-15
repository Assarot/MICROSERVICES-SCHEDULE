package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_course_management.application.ports.input.CourseTypeServicePort;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.mapper.CourseTypeRestMapper;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request.CourseTypeCreateRequest;;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.respose.CourseTypeResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course-type")
public class CourseTypeRestAdapter {

    private final CourseTypeServicePort servicePort;
    private final CourseTypeRestMapper restMapper;

    @GetMapping("/v1/api")
    public List<CourseTypeResponse> findAll() {
        return restMapper.toCourseTypeResposeList(servicePort.findAll());
    }

    @PostMapping("/v1/api")
    public ResponseEntity<CourseTypeResponse> save(@Valid @RequestBody CourseTypeCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toCourseTypeResponse(
                        servicePort.save(restMapper.toCourseType(request))));
    }

    @PutMapping("/v1/api/{id}")
    public CourseTypeResponse update(@PathVariable Long id, @Valid @RequestBody CourseTypeCreateRequest request) {
        return restMapper.toCourseTypeResponse(servicePort.update(id, restMapper.toCourseType(request)));
    }

    @DeleteMapping("/v1/api/{id}")
    public void delete(@PathVariable Long id) {
        servicePort.deleteById(id);
    }
}
