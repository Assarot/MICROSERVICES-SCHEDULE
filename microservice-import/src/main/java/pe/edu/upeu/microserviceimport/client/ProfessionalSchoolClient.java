package pe.edu.upeu.microserviceimport.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pe.edu.upeu.microserviceimport.dto.external.ProfessionalSchoolCreateRequestDTO;
import pe.edu.upeu.microserviceimport.dto.external.ProfessionalSchoolResponseDTO;

import java.util.List;

@FeignClient(name = "MS-COURSE-MANAGEMENT", url = "${ms.course-management.url:http://MS-COURSE-MANAGEMENT}", contextId = "professionalSchoolClient")
public interface ProfessionalSchoolClient {

    @GetMapping("/professional-school/v1/api")
    List<ProfessionalSchoolResponseDTO> findAll();

    @GetMapping("/professional-school/v1/api/{id}")
    ProfessionalSchoolResponseDTO findById(@PathVariable("id") Long id);

    @PostMapping("/professional-school/v1/api")
    ProfessionalSchoolResponseDTO create(@RequestBody ProfessionalSchoolCreateRequestDTO req);
}
