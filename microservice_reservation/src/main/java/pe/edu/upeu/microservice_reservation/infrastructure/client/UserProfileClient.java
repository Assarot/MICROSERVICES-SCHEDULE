package pe.edu.upeu.microservice_reservation.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.upeu.microservice_reservation.infrastructure.client.dto.UserProfileClientDto;

import java.util.List;

/**
 * Feign client para comunicarse con MS-USER.
 * Nombre Eureka: ms-user (puerto 8082)
 */
@FeignClient(name = "ms-user")
public interface UserProfileClient {

    @GetMapping("/api/v1/user-profiles/{id}")
    UserProfileClientDto getUserProfileById(@PathVariable("id") Long id);

    @GetMapping("/api/v1/user-profiles/active")
    List<UserProfileClientDto> getAllActiveUserProfiles();
}
