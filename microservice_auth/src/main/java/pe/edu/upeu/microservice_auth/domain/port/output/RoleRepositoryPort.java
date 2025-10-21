package pe.edu.upeu.microservice_auth.domain.port.output;

import pe.edu.upeu.microservice_auth.domain.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepositoryPort {
    
    Role save(Role role);
    
    Optional<Role> findById(Long id);
    
    Optional<Role> findByName(String name);
    
    List<Role> findAll();
    
    void deleteById(Long id);
    
    boolean existsByName(String name);
}
