package pe.edu.upeu.microservice_user.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.upeu.microservice_user.infrastructure.client.dto.AuthUserResponseDto;

@FeignClient(name = "microservice-auth")
public interface AuthUserClient {

    @GetMapping("/api/auth/users/by-profile/{profileId}")
    AuthUserResponseDto getUserByProfileId(@PathVariable("profileId") Long profileId);
}
