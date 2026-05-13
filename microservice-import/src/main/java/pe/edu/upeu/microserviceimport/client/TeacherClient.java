package pe.edu.upeu.microserviceimport.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microserviceimport.dto.TeacherDTO;

import java.util.List;

@FeignClient(name = "MS-COURSE-MANAGEMENT", contextId = "teacherClient")
public interface TeacherClient {

    @GetMapping("/teacher/v1/api")
    List<TeacherDTO> getAllTeachers();

    @PostMapping("/teacher/v1/api")
    TeacherDTO createTeacher(@RequestBody TeacherDTO teacherDTO);

    @GetMapping("/teacher/v1/api/{id}")
    TeacherDTO getTeacherById(@PathVariable("id") Long id);
}
