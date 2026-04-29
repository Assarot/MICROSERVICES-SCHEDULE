package pe.edu.upeu.microserviceimport.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pe.edu.upeu.microserviceimport.dto.CourseAssignmentCourseDTO;

@FeignClient(name = "microservice-course-management", contextId = "courseAssignmentCourseClient")
public interface CourseAssignmentCourseClient {

    @PostMapping("/course-assignment-course/v1/api")
    CourseAssignmentCourseDTO create(@RequestBody CourseAssignmentCourseDTO request);
}