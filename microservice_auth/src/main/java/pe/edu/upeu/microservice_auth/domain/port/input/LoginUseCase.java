package pe.edu.upeu.microservice_auth.domain.port.input;

import pe.edu.upeu.microservice_auth.domain.model.AuthUser;

import java.util.Map;

public interface LoginUseCase {
    
    Map<String, Object> login(String username, String password);
    
    AuthUser getUserByUsername(String username);
}
