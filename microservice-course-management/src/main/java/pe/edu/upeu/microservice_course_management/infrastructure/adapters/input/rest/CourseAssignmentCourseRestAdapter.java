package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_course_management.application.ports.input.CourseAssignmentCourseServicePort;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.mapper.CourseAssignmentCourseRestMapper;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request.CourseAssignmentCourseCreateRequest;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.respose.CourseAssignmentCourseResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course-assignment-course")
public class CourseAssignmentCourseRestAdapter {

    private final CourseAssignmentCourseServicePort servicePort;
    private final CourseAssignmentCourseRestMapper restMapper;

    @GetMapping("/v1/api")
    public List<CourseAssignmentCourseResponse> findAll(){
        return restMapper.toCourseAssignmentCourseResponseList(servicePort.findAll());
    }

    @GetMapping("/v1/api/{id}")
    public CourseAssignmentCourseResponse findById(@PathVariable("id") Long id){
        return restMapper.toCourseAssignmentCourseResponse(servicePort.findById(id));
    }

    @PostMapping("/v1/api")
    public ResponseEntity<CourseAssignmentCourseResponse> create(@RequestBody CourseAssignmentCourseCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toCourseAssignmentCourseResponse(
                        servicePort.save(restMapper.toCourseAssignmentCourse(request))));
    }

    @PutMapping("/v1/api/{id}")
    public CourseAssignmentCourseResponse update(@PathVariable("id") Long id, @RequestBody CourseAssignmentCourseCreateRequest request){
        return restMapper.toCourseAssignmentCourseResponse(servicePort.update(id,restMapper.toCourseAssignmentCourse(request)));
    }

    @DeleteMapping("/v1/api/{id}")
    public void delete(@PathVariable("id") Long id){servicePort.deleteById(id);}
}
