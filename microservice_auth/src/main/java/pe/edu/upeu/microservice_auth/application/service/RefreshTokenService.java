package pe.edu.upeu.microservice_auth.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.microservice_auth.domain.exception.InvalidTokenException;
import pe.edu.upeu.microservice_auth.domain.model.AuthUser;
import pe.edu.upeu.microservice_auth.domain.model.RefreshToken;
import pe.edu.upeu.microservice_auth.domain.port.input.RefreshTokenUseCase;
import pe.edu.upeu.microservice_auth.domain.port.output.JwtServicePort;
import pe.edu.upeu.microservice_auth.domain.port.output.RefreshTokenRepositoryPort;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService implements RefreshTokenUseCase {

    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;
    private final JwtServicePort jwtServicePort;

    @Override
    @Transactional
    public Map<String, Object> refreshAccessToken(String refreshTokenValue) {
        log.info("Attempting to refresh access token");

        RefreshToken refreshToken = refreshTokenRepositoryPort.findByRefreshToken(refreshTokenValue)
                .orElseThrow(() -> new InvalidTokenException("Refresh token not found"));

        if (refreshToken.isExpired()) {
            refreshTokenRepositoryPort.deleteByRefreshToken(refreshTokenValue);
            log.warn("Refresh token expired");
            throw new InvalidTokenException("Refresh token expired");
        }

        AuthUser user = refreshToken.getAuthUser();

        if (!user.getIsActive()) {
            log.warn("Inactive user tried to refresh token: {}", user.getUsername());
            throw new InvalidTokenException("User account is inactive");
        }

        // Generate new access token
        String newAccessToken = jwtServicePort.generateAccessToken(user);

        // Optional: Implement refresh token rotation
        String newRefreshToken = jwtServicePort.generateRefreshToken(user);
        refreshToken.setRefreshToken(newRefreshToken);
        refreshToken.setExpiryDate(Instant.now().plusMillis(jwtServicePort.getRefreshTokenExpiration()));
        refreshTokenRepositoryPort.save(refreshToken);

        log.info("Access token refreshed successfully for user: {}", user.getUsername());

        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", newAccessToken);
        response.put("refreshToken", newRefreshToken);
        response.put("expiresIn", jwtServicePort.getAccessTokenExpiration() / 1000);

        return response;
    }

    @Override
    @Transactional
    public void revokeRefreshToken(String refreshToken) {
        log.info("Revoking refresh token");
        refreshTokenRepositoryPort.deleteByRefreshToken(refreshToken);
    }

    @Override
    @Transactional
    public void revokeAllUserTokens(Long userId) {
        log.info("Revoking all tokens for user id: {}", userId);
        refreshTokenRepositoryPort.deleteByAuthUserId(userId);
    }
}
