package pe.edu.upeu.microservice_auth.domain.port.output;

import pe.edu.upeu.microservice_auth.domain.model.AuthUser;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
    
    AuthUser save(AuthUser user);
    
    Optional<AuthUser> findById(Long id);
    
    Optional<AuthUser> findByUsername(String username);
    
    List<AuthUser> findAll();
    
    void deleteById(Long id);
    
    boolean existsByUsername(String username);
}
