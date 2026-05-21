package pe.edu.upeu.microserviceimport.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pe.edu.upeu.microserviceimport.dto.external.PlanCreateRequestDTO;
import pe.edu.upeu.microserviceimport.dto.external.PlanResponseDTO;

import java.util.List;

@FeignClient(name = "MS-COURSE-MANAGEMENT", url = "${ms.course-management.url:http://MS-COURSE-MANAGEMENT}", contextId = "planClient")
public interface PlanClient {

    @GetMapping("/plan/v1/api")
    List<PlanResponseDTO> findAll();

    @GetMapping("/plan/v1/api/{id}")
    PlanResponseDTO findById(@PathVariable("id") Long id);

    @PostMapping("/plan/v1/api")
    PlanResponseDTO create(@RequestBody PlanCreateRequestDTO req);
}
