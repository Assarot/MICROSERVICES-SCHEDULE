package pe.edu.upeu.microservice_auth.domain.port.input;

import java.util.Map;

public interface RefreshTokenUseCase {
    
    Map<String, Object> refreshAccessToken(String refreshToken);
    
    void revokeRefreshToken(String refreshToken);
    
    void revokeAllUserTokens(Long userId);
}
