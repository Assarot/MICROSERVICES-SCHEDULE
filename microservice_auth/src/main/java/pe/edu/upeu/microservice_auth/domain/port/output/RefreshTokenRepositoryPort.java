package pe.edu.upeu.microservice_auth.domain.port.output;

import pe.edu.upeu.microservice_auth.domain.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepositoryPort {
    
    RefreshToken save(RefreshToken refreshToken);
    
    Optional<RefreshToken> findByRefreshToken(String token);
    
    void deleteByAuthUserId(Long userId);
    
    void deleteByRefreshToken(String token);
    
    void deleteExpiredTokens();
}
