package pe.edu.upeu.microservice_auth.domain.port.output;

import pe.edu.upeu.microservice_auth.domain.model.AuthSession;

import java.util.Optional;

public interface AuthSessionRepositoryPort {
    
    AuthSession save(AuthSession authSession);
    
    Optional<AuthSession> findByToken(String token);
    
    void deleteByAuthUserId(Long userId);
    
    void deleteByToken(String token);
    
    void deleteExpiredSessions();
}
