package pe.edu.upeu.microserviceimport.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import pe.edu.upeu.microserviceimport.dto.CourseAssignmentDTO;
import pe.edu.upeu.microserviceimport.dto.CourseResponseDTO;
import pe.edu.upeu.microserviceimport.dto.CreateCourseDTO;

import java.util.List;

@FeignClient(name = "MS-COURSE-MANAGEMENT", url = "${ms.course-management.url:http://MS-COURSE-MANAGEMENT}", contextId = "courseManagementClient")
public interface CourseManagementClient {

    @PostMapping("/course/v1/api")
    CourseResponseDTO createCourse(@RequestBody CreateCourseDTO courseDTO);

    @GetMapping("/course/v1/api")
    List<CourseResponseDTO> getAllCourses();

    @GetMapping("/course/v1/api/{id}")
    CourseResponseDTO getCourseById(@PathVariable("id") Long id);

    @PostMapping("/course-assignment/v1/api")
    CourseAssignmentDTO createCourseAssignment(@RequestBody CourseAssignmentDTO assignmentDTO);

    @GetMapping("/course-assignment/v1/api/{id}")
    CourseAssignmentDTO getCourseAssignmentById(@PathVariable("id") Long id);

    @GetMapping("/course-assignment-course/v1/api")
    List<pe.edu.upeu.microserviceimport.dto.response.CourseAssignmentCourseResponseDTO> getAllCourseAssignmentCourses();

    @GetMapping("/course-assignment/v1/api")
    List<pe.edu.upeu.microserviceimport.dto.response.CourseAssignmentResponseDTO> getAllCourseAssignments();
}
