package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.external.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.external.feign.dto.EnvAcademicSpaceDTO;

@FeignClient(name = "ms-enviroment", path = "/v1/api/academic-space")
public interface EnvironmentFeignClient {

    @GetMapping("/{id}")
    EnvAcademicSpaceDTO getById(@PathVariable("id") Long id);
}
