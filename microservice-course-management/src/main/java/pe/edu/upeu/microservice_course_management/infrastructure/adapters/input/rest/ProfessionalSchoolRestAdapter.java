package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_course_management.application.ports.input.ProfessionalSchoolServicePort;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.mapper.ProfessionalSchoolRestMapper;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request.ProfessionalSchoolCreateRequest;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.respose.ProfessionalSchoolResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/professional-school")
public class ProfessionalSchoolRestAdapter {

    private final ProfessionalSchoolServicePort servicePort;
    private final ProfessionalSchoolRestMapper restMapper;

    @GetMapping("/v1/api")
    public List<ProfessionalSchoolResponse> findAll() {
        return restMapper.toProfessionalSchoolResponseList(servicePort.findAll());
    }

    @GetMapping("/v1/api/{id}")
    public ProfessionalSchoolResponse findById(@PathVariable Long id) {
        return restMapper.toProfessionalSchoolResponse(servicePort.findById(id));
    }

    @PostMapping("/v1/api")
    public ResponseEntity<ProfessionalSchoolResponse> save(@Valid @RequestBody ProfessionalSchoolCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toProfessionalSchoolResponse(
                        servicePort.save(restMapper.toProfessionalSchool(request))));
    }

    @PutMapping("/v1/api/{id}")
    public ProfessionalSchoolResponse update(@PathVariable Long id, @Valid @RequestBody ProfessionalSchoolCreateRequest request) {
        return restMapper.toProfessionalSchoolResponse(servicePort.update(id, restMapper.toProfessionalSchool(request)));
    }

    @DeleteMapping("/v1/api/{id}")
    public void delete(@PathVariable Long id) {
        servicePort.deleteById(id);
    }

}
