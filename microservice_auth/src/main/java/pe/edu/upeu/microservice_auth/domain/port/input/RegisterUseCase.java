package pe.edu.upeu.microservice_auth.domain.port.input;

import pe.edu.upeu.microservice_auth.domain.model.AuthUser;

public interface RegisterUseCase {
    
    AuthUser register(String username, String password, Long userProfileId);
}
