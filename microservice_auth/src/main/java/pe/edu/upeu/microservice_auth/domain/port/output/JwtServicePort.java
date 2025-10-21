package pe.edu.upeu.microservice_auth.domain.port.output;

import pe.edu.upeu.microservice_auth.domain.model.AuthUser;

import java.util.Map;

public interface JwtServicePort {
    
    String generateAccessToken(AuthUser user);
    
    String generateRefreshToken(AuthUser user);
    
    String extractUsername(String token);
    
    boolean validateToken(String token, AuthUser user);
    
    boolean isTokenExpired(String token);
    
    Map<String, Object> extractAllClaims(String token);
    
    long getAccessTokenExpiration();
    
    long getRefreshTokenExpiration();
}
