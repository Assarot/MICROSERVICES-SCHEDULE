package pe.edu.upeu.microservice_auth.application.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_auth.domain.port.output.AuthSessionRepositoryPort;
import pe.edu.upeu.microservice_auth.domain.port.output.RefreshTokenRepositoryPort;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenSessionScheduler {

    private final AuthSessionRepositoryPort authSessionRepositoryPort;
    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;

    @Scheduled(fixedRate = 3600000)
    public void deactivateExpiredSessions() {
        Instant now = Instant.now();
        log.info("Running scheduled deactivation for expired sessions and refresh tokens at {}", now);
        authSessionRepositoryPort.deactivateExpiredSessions(now);
        refreshTokenRepositoryPort.deactivateExpiredTokens();
    }
}
