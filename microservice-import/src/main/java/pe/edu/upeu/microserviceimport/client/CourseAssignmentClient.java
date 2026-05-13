package pe.edu.upeu.microserviceimport.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import pe.edu.upeu.microserviceimport.dto.CourseAssignmentDTO;

@FeignClient(name = "MS-COURSE-MANAGEMENT", contextId = "courseAssignmentClient", path = "/api/v1/course-assignments")
public interface CourseAssignmentClient {

    @PostMapping
    CourseAssignmentDTO createCourseAssignment(@RequestBody CourseAssignmentDTO assignmentDTO);

    @GetMapping("/{id}")
    CourseAssignmentDTO getCourseAssignmentById(@PathVariable("id") Long id);
}
