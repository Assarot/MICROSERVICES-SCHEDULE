package pe.edu.upeu.microservice_user.infrastructure.rest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_user.application.service.UserProfileService;
import pe.edu.upeu.microservice_user.infrastructure.client.dto.AuthUserResponseDto;

@RestController
@RequestMapping("/api/users/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AuthUserLookupController {

    private final UserProfileService userProfileService;

    @GetMapping("/by-profile/{id}")
    public ResponseEntity<AuthUserResponseDto> getAuthUserByProfileId(@PathVariable("id") Long id) {
        log.info("GET /api/users/auth/by-profile/{} - Consultando auth_user por profileId", id);
        AuthUserResponseDto dto = userProfileService.getAuthUserByProfileId(id);
        return ResponseEntity.ok(dto);
    }
}
