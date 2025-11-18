package pe.edu.upeu.microservice_auth.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.microservice_auth.domain.exception.InvalidCredentialsException;
import pe.edu.upeu.microservice_auth.domain.exception.InvalidTokenException;
import pe.edu.upeu.microservice_auth.domain.exception.UserNotFoundException;
import pe.edu.upeu.microservice_auth.domain.model.AuthSession;
import pe.edu.upeu.microservice_auth.domain.model.AuthUser;
import pe.edu.upeu.microservice_auth.domain.model.RefreshToken;
import pe.edu.upeu.microservice_auth.domain.port.output.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final JwtServicePort jwtServicePort;
    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;
    private final AuthSessionRepositoryPort authSessionRepositoryPort;

    @Transactional
    public Map<String, Object> login(String username, String password) {
        AuthUser user = authenticate(username, password);

        String accessToken = jwtServicePort.generateAccessToken(user);

        AuthSession session = AuthSession.builder()
                .token(accessToken)
                .authUser(user)
                .expiresIn(Instant.now().plusMillis(jwtServicePort.getAccessTokenExpiration()))
                .isActive(true)
                .build();
        authSessionRepositoryPort.save(session);

        Map<String, Object> response = new HashMap<>();
        response.put("access_token", accessToken);
        response.put("token_type", "Bearer");
        response.put("expires_in", jwtServicePort.getAccessTokenExpiration() / 1000);
        return response;
    }

    @Transactional
    public Map<String, Object> loginWithRememberMe(String username, String password) {
        AuthUser user = authenticate(username, password);

        String accessToken = jwtServicePort.generateAccessToken(user);

        AuthSession session = AuthSession.builder()
                .token(accessToken)
                .authUser(user)
                .expiresIn(Instant.now().plusMillis(jwtServicePort.getAccessTokenExpiration()))
                .isActive(true)
                .build();
        session = authSessionRepositoryPort.save(session);

        String refreshTokenStr = jwtServicePort.generateRefreshToken(user);
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(refreshTokenStr)
                .authSession(session)
                .expiryDate(Instant.now().plusMillis(jwtServicePort.getRefreshTokenExpiration()))
                .isActive(true)
                .build();
        refreshTokenRepositoryPort.save(refreshToken);

        Map<String, Object> response = new HashMap<>();
        response.put("access_token", accessToken);
        response.put("refresh_token", refreshTokenStr);
        response.put("token_type", "Bearer");
        response.put("expires_in", jwtServicePort.getAccessTokenExpiration() / 1000);
        return response;
    }

    @Transactional
    public Map<String, Object> refreshAccessToken(String refreshTokenStr) {
        RefreshToken rt = refreshTokenRepositoryPort.findActiveByRefreshToken(refreshTokenStr)
                .orElseThrow(() -> new InvalidTokenException("Refresh token not found or inactive"));

        if (rt.isExpired()) {
            log.warn("Refresh token expired");
            refreshTokenRepositoryPort.deactivateByRefreshToken(refreshTokenStr);
            throw new InvalidTokenException("Refresh token expired");
        }

        AuthUser user = Optional.ofNullable(rt.getAuthSession())
                .map(AuthSession::getAuthUser)
                .orElseThrow(() -> new InvalidTokenException("Refresh token not bound to a session"));
        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new InvalidTokenException("User inactive");
        }

        String newAccess = jwtServicePort.generateAccessToken(user);

        // Update bound session's access token if available
        Optional.ofNullable(rt.getAuthSession()).ifPresent(session -> {
            session.setToken(newAccess);
            session.setExpiresIn(Instant.now().plusMillis(jwtServicePort.getAccessTokenExpiration()));
            authSessionRepositoryPort.save(session);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("access_token", newAccess);
        response.put("token_type", "Bearer");
        response.put("expires_in", jwtServicePort.getAccessTokenExpiration() / 1000);
        return response;
    }

    @Transactional
    public void logout(String accessToken, String refreshTokenStr) {
        if (accessToken != null && !accessToken.isBlank()) {
            authSessionRepositoryPort.findByToken(accessToken).ifPresent(session -> {
                session.setIsActive(false);
                session.setLogoutAt(Instant.now());
                authSessionRepositoryPort.save(session);
            });
        }
        if (refreshTokenStr != null && !refreshTokenStr.isBlank()) {
            refreshTokenRepositoryPort.deactivateByRefreshToken(refreshTokenStr);
        }
    }

    private AuthUser authenticate(String username, String password) {
        AuthUser user = userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new InvalidCredentialsException("User account is inactive");
        }
        if (!passwordEncoderPort.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException();
        }
        return user;
    }

    public AuthUser getUserByUsername(String username) {
        return userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }
}
