package pe.edu.upeu.microserviceimport.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pe.edu.upeu.microserviceimport.dto.external.CycleCreateRequestDTO;
import pe.edu.upeu.microserviceimport.dto.external.CycleResponseDTO;

import java.util.List;

@FeignClient(name = "MS-COURSE-MANAGEMENT", url = "${ms.course-management.url:http://MS-COURSE-MANAGEMENT}", contextId = "cycleClient")
public interface CycleClient {

    @GetMapping("/cycle/v1/api")
    List<CycleResponseDTO> findAll();

    @PostMapping("/cycle/v1/api")
    CycleResponseDTO createCycle(@RequestBody CycleCreateRequestDTO request);

}
