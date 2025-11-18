package pe.edu.upeu.microservice_auth.infrastructure.adapter.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.microservice_auth.domain.model.AuthUser;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<AuthUser, Long> {
    
    Optional<AuthUser> findByUsername(String username);
    
    Optional<AuthUser> findByIdUserProfile(Long idUserProfile);
    
    boolean existsByUsername(String username);
}
