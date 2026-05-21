package pe.edu.upeu.microserviceimport.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pe.edu.upeu.microserviceimport.dto.external.CourseTypeCreateRequestDTO;
import pe.edu.upeu.microserviceimport.dto.external.CourseTypeResponseDTO;

import java.util.List;

@FeignClient(name = "MS-COURSE-MANAGEMENT", url = "${ms.course-management.url:http://MS-COURSE-MANAGEMENT}", contextId = "courseTypeClient")
public interface CourseTypeClient {

    @GetMapping("/course-type/v1/api")
    List<CourseTypeResponseDTO> findAll();

    @PostMapping("/course-type/v1/api")
    CourseTypeResponseDTO create(@RequestBody CourseTypeCreateRequestDTO request);
}
