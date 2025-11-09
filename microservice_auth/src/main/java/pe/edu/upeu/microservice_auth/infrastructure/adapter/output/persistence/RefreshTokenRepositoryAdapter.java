package pe.edu.upeu.microservice_auth.infrastructure.adapter.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.microservice_auth.domain.model.RefreshToken;
import pe.edu.upeu.microservice_auth.domain.port.output.RefreshTokenRepositoryPort;

import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepositoryPort {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenJpaRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByRefreshToken(String token) {
        return refreshTokenJpaRepository.findByRefreshToken(token);
    }

    @Override
    public Optional<RefreshToken> findActiveByRefreshToken(String token) {
        return refreshTokenJpaRepository.findActiveByRefreshToken(token);
    }

    @Override
    @Transactional
    public void deleteByAuthUserId(Long userId) {
        refreshTokenJpaRepository.deleteByAuthUser_IdAuthUser(userId);
    }

    @Override
    @Transactional
    public void deleteByRefreshToken(String token) {
        refreshTokenJpaRepository.deleteByRefreshToken(token);
    }

    @Override
    @Transactional
    public void deleteExpiredTokens() {
        refreshTokenJpaRepository.deleteExpiredTokens(Instant.now());
    }

    @Override
    @Transactional
    public void deactivateByRefreshToken(String token) {
        refreshTokenJpaRepository.deactivateByRefreshToken(token, Instant.now());
    }

    @Override
    @Transactional
    public void deactivateExpiredTokens() {
        refreshTokenJpaRepository.deactivateExpiredTokens(Instant.now());
    }
}
