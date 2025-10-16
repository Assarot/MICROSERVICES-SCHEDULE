package pe.edu.upeu.microservice_auth.infrastructure.adapter.input.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_auth.domain.model.AuthUser;
import pe.edu.upeu.microservice_auth.domain.port.input.LoginUseCase;
import pe.edu.upeu.microservice_auth.domain.port.input.RefreshTokenUseCase;
import pe.edu.upeu.microservice_auth.domain.port.input.RegisterUseCase;
import pe.edu.upeu.microservice_auth.infrastructure.adapter.input.dto.*;
import pe.edu.upeu.microservice_auth.infrastructure.adapter.input.mapper.UserMapper;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUseCase registerUseCase;
    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        log.info("POST /api/auth/register - username: {}", registerDTO.getUsername());

        AuthUser user = registerUseCase.register(
                registerDTO.getUsername(),
                registerDTO.getPassword(),
                registerDTO.getUserProfileId()
        );

        // After registration, automatically log in the user
        Map<String, Object> loginResponse = loginUseCase.login(
                registerDTO.getUsername(),
                registerDTO.getPassword()
        );

        AuthResponseDTO response = AuthResponseDTO.builder()
                .accessToken((String) loginResponse.get("accessToken"))
                .refreshToken((String) loginResponse.get("refreshToken"))
                .expiresIn(((Number) loginResponse.get("expiresIn")).longValue())
                .user(userMapper.toResponseDTO(user))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        log.info("POST /api/auth/login - username: {}", loginDTO.getUsername());

        Map<String, Object> loginResponse = loginUseCase.login(
                loginDTO.getUsername(),
                loginDTO.getPassword()
        );

        AuthUser user = loginUseCase.getUserByUsername(loginDTO.getUsername());

        AuthResponseDTO response = AuthResponseDTO.builder()
                .accessToken((String) loginResponse.get("accessToken"))
                .refreshToken((String) loginResponse.get("refreshToken"))
                .expiresIn(((Number) loginResponse.get("expiresIn")).longValue())
                .user(userMapper.toResponseDTO(user))
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, Object>> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO request) {
        log.info("POST /api/auth/refresh-token");

        Map<String, Object> response = refreshTokenUseCase.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@Valid @RequestBody RefreshTokenRequestDTO request) {
        log.info("POST /api/auth/logout");

        refreshTokenUseCase.revokeRefreshToken(request.getRefreshToken());
        
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}
