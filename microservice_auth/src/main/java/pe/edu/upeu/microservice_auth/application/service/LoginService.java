package pe.edu.upeu.microservice_auth.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.microservice_auth.domain.exception.InvalidCredentialsException;
import pe.edu.upeu.microservice_auth.domain.exception.UserNotFoundException;
import pe.edu.upeu.microservice_auth.domain.model.AuthUser;
import pe.edu.upeu.microservice_auth.domain.model.RefreshToken;
import pe.edu.upeu.microservice_auth.domain.port.input.LoginUseCase;
import pe.edu.upeu.microservice_auth.domain.port.output.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final JwtServicePort jwtServicePort;
    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;

    @Override
    @Transactional
    public Map<String, Object> login(String username, String password) {
        log.info("Login attempt for user: {}", username);

        AuthUser user = userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (!user.getIsActive()) {
            log.warn("Inactive user tried to login: {}", username);
            throw new InvalidCredentialsException("User account is inactive");
        }

        if (!passwordEncoderPort.matches(password, user.getPassword())) {
            user.incrementFailedAttempts();
            userRepositoryPort.save(user);
            log.warn("Invalid credentials for user: {}", username);
            throw new InvalidCredentialsException();
        }

        // Reset failed attempts on successful login
        user.resetFailedAttempts();
        userRepositoryPort.save(user);

        // Generate tokens
        String accessToken = jwtServicePort.generateAccessToken(user);
        String refreshTokenValue = jwtServicePort.generateRefreshToken(user);

        // Save refresh token to database
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(refreshTokenValue)
                .authUser(user)
                .expiryDate(Instant.now().plusMillis(jwtServicePort.getRefreshTokenExpiration()))
                .build();
        refreshTokenRepositoryPort.save(refreshToken);

        log.info("User logged in successfully: {}", username);

        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshTokenValue);
        response.put("expiresIn", jwtServicePort.getAccessTokenExpiration() / 1000); // in seconds
        response.put("user", sanitizeUser(user));

        return response;
    }

    @Override
    public AuthUser getUserByUsername(String username) {
        return userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    private Map<String, Object> sanitizeUser(AuthUser user) {
        Map<String, Object> sanitized = new HashMap<>();
        sanitized.put("id", user.getIdAuthUser());
        sanitized.put("username", user.getUsername());
        sanitized.put("isActive", user.getIsActive());
        sanitized.put("idUserProfile", user.getIdUserProfile());
        sanitized.put("roles", user.getRoles().stream()
                .map(role -> {
                    Map<String, Object> roleMap = new HashMap<>();
                    roleMap.put("id", role.getIdRole());
                    roleMap.put("name", role.getName());
                    return roleMap;
                })
                .toList());
        return sanitized;
    }
}
