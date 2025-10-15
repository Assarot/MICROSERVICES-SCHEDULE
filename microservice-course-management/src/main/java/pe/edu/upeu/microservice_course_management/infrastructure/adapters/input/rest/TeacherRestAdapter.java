package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_course_management.application.ports.input.TeacherServicePort;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.mapper.TeacherRestMapper;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request.TeacherCreateRequest;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.respose.TeacherResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teacher")
public class TeacherRestAdapter {

    private final TeacherServicePort servicePort;
    private final TeacherRestMapper restMapper;

    @GetMapping("/v1/api")
    public List<TeacherResponse> findAll() {
        return restMapper.toTeacherResponseList(servicePort.findAll());
    }

    @GetMapping("/v1/api/{id}")
    public TeacherResponse findById(@PathVariable Long id) {
        return restMapper.toTeacherResponse(servicePort.findById(id));
    }

    @PostMapping("/v1/api")
    public ResponseEntity<TeacherResponse> save(@Valid @RequestBody TeacherCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toTeacherResponse(
                        servicePort.save(restMapper.toTeacher(request))));
    }

    @PutMapping("/v1/api/{id}")
    public TeacherResponse update(@PathVariable Long id, @Valid @RequestBody TeacherCreateRequest request) {
        return restMapper.toTeacherResponse(servicePort.update(id, restMapper.toTeacher(request)));
    }

    @DeleteMapping("/v1/api/{id}")
    public void delete(@PathVariable Long id) {
        servicePort.deleteById(id);
    }
}
