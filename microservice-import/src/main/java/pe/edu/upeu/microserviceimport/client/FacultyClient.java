package pe.edu.upeu.microserviceimport.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pe.edu.upeu.microserviceimport.dto.external.FacultyCreateRequestDTO;
import pe.edu.upeu.microserviceimport.dto.external.FacultyResponseDTO;

import java.util.List;

@FeignClient(name = "MS-COURSE-MANAGEMENT", url = "${ms.course-management.url:http://MS-COURSE-MANAGEMENT}", contextId = "facultyClient")
public interface FacultyClient {

    @GetMapping("/faculty/v1/api")
    List<FacultyResponseDTO> findAll();

    @GetMapping("/faculty/v1/api/{id}")
    FacultyResponseDTO findById(@PathVariable("id") Long id);

    @PostMapping("/faculty/v1/api")
    FacultyResponseDTO create(@RequestBody FacultyCreateRequestDTO req);
}
