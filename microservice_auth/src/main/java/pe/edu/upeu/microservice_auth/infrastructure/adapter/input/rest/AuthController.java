package pe.edu.upeu.microservice_auth.infrastructure.adapter.input.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_auth.domain.model.AuthUser;
import pe.edu.upeu.microservice_auth.domain.port.input.RegisterUseCase;
import pe.edu.upeu.microservice_auth.application.service.AuthService;
import pe.edu.upeu.microservice_auth.infrastructure.adapter.input.dto.*;
import pe.edu.upeu.microservice_auth.infrastructure.adapter.input.mapper.UserMapper;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUseCase registerUseCase;
    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        log.info("POST /api/auth/register - username: {}", registerDTO.getUsername());

        AuthUser user = registerUseCase.register(
                registerDTO.getUsername(),
                registerDTO.getPassword(),
                registerDTO.getUserProfileId()
        );

        Map<String, Object> loginResponse = authService.loginWithRememberMe(
                registerDTO.getUsername(),
                registerDTO.getPassword()
        );

        AuthResponseDTO response = AuthResponseDTO.builder()
                .accessToken((String) loginResponse.get("access_token"))
                .refreshToken((String) loginResponse.get("refresh_token"))
                .expiresIn(((Number) loginResponse.get("expires_in")).longValue())
                .user(userMapper.toResponseDTO(user))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

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
