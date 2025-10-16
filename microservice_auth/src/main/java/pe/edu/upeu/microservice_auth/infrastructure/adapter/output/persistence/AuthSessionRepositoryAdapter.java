package pe.edu.upeu.microservice_auth.infrastructure.adapter.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.microservice_auth.domain.model.AuthSession;
import pe.edu.upeu.microservice_auth.domain.port.output.AuthSessionRepositoryPort;

import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthSessionRepositoryAdapter implements AuthSessionRepositoryPort {

    private final AuthSessionJpaRepository authSessionJpaRepository;

    @Override
    public AuthSession save(AuthSession authSession) {
        return authSessionJpaRepository.save(authSession);
    }

    @Override
    public Optional<AuthSession> findByToken(String token) {
        return authSessionJpaRepository.findByToken(token);
    }

    @Override
    @Transactional
    public void deleteByAuthUserId(Long userId) {
        authSessionJpaRepository.deleteByAuthUser_IdAuthUser(userId);
    }

    @Override
    @Transactional
    public void deleteByToken(String token) {
        authSessionJpaRepository.deleteByToken(token);
    }

    @Override
    @Transactional
    public void deleteExpiredSessions() {
        authSessionJpaRepository.deleteExpiredSessions(Instant.now());
    }
}
