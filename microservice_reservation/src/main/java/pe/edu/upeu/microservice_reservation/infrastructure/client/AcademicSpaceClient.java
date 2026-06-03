package pe.edu.upeu.microservice_reservation.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.upeu.microservice_reservation.infrastructure.client.dto.AcademicSpaceClientDto;

import java.util.List;

/**
 * Feign client para comunicarse con MS-ENVIRONMENT.
 * Nombre Eureka: ms-enviroment (puerto 8087)
 * Nota: el nombre en Eureka está escrito sin 'n' (enviroment) — respetamos ese nombre.
 */
@FeignClient(name = "ms-enviroment")
public interface AcademicSpaceClient {

    @GetMapping("/v1/api/academic-space/{id}")
    AcademicSpaceClientDto getAcademicSpaceById(@PathVariable("id") Long id);

    @GetMapping("/v1/api/academic-space")
    List<AcademicSpaceClientDto> getAllAcademicSpaces();
}
