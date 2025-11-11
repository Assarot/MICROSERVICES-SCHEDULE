package pe.edu.upeu.microservice_auth.infrastructure.adapter.input.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import pe.edu.upeu.microservice_auth.domain.model.AuthUser;
import pe.edu.upeu.microservice_auth.application.service.AuthService;
import pe.edu.upeu.microservice_auth.infrastructure.adapter.input.dto.*;
import pe.edu.upeu.microservice_auth.infrastructure.adapter.input.mapper.UserMapper;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        log.info("POST /api/auth/login - username: {}", loginDTO.getUsername());
        Map<String, Object> response = authService.login(loginDTO.getUsername(), loginDTO.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/remember")
    public ResponseEntity<Map<String, Object>> loginRemember(@Valid @RequestBody UserLoginDTO loginDTO) {
        log.info("POST /api/auth/login/remember - username: {}", loginDTO.getUsername());
        Map<String, Object> response = authService.loginWithRememberMe(loginDTO.getUsername(), loginDTO.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO request) {
        log.info("POST /api/auth/refresh");
        Map<String, Object> response = authService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@Valid @RequestBody LogoutRequestDTO request) {
        log.info("POST /api/auth/logout");
        authService.logout(request.getAccessToken(), request.getRefreshToken());
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me(Authentication authentication) {
        AuthUser user = authService.getUserByUsername(authentication.getName());
        return ResponseEntity.ok(userMapper.toResponseDTO(user));
    }
}
