package pe.edu.upeu.microserviceimport.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pe.edu.upeu.microserviceimport.dto.AcademicSpaceDTO;

import java.util.List;

@FeignClient(name = "microservice-enviroment", contextId = "environmentClient")
public interface EnvironmentClient {

    @GetMapping("/v1/api/academic-space")
    List<AcademicSpaceDTO> getAllAcademicSpaces();

    @GetMapping("/v1/api/academic-space/{id}")
    AcademicSpaceDTO getAcademicSpaceById(@PathVariable("id") Long id);

    @PostMapping("/v1/api/academic-space")
    AcademicSpaceDTO createAcademicSpace(@RequestBody AcademicSpaceDTO spaceDTO);
}
