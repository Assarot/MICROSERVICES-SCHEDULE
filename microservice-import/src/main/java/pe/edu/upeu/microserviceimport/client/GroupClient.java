package pe.edu.upeu.microserviceimport.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pe.edu.upeu.microserviceimport.dto.external.GroupCreateRequestDTO;
import pe.edu.upeu.microserviceimport.dto.external.GroupResponseDTO;

import java.util.List;

@FeignClient(name = "MS-COURSE-MANAGEMENT", url = "${ms.course-management.url:http://MS-COURSE-MANAGEMENT}", contextId = "groupClient")
public interface GroupClient {

    @GetMapping("/group/v1/api")
    List<GroupResponseDTO> findAll();

    @PostMapping("/group/v1/api")
    GroupResponseDTO createGroup(@RequestBody GroupCreateRequestDTO request);

}
