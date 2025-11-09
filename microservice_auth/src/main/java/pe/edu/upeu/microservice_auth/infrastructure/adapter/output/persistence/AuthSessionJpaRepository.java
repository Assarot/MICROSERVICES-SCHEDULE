package pe.edu.upeu.microservice_auth.infrastructure.adapter.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.microservice_auth.domain.model.AuthSession;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface AuthSessionJpaRepository extends JpaRepository<AuthSession, Long> {
    
    Optional<AuthSession> findByToken(String token);
    
    void deleteByAuthUser_IdAuthUser(Long userId);
    
    void deleteByToken(String token);
    
    @Modifying
    @Query("DELETE FROM AuthSession a WHERE a.expiresIn < :now")
    void deleteExpiredSessions(@Param("now") Instant now);

    @Modifying
    @Query("UPDATE AuthSession a SET a.isActive = false, a.logoutAt = :now WHERE a.expiresIn < :now AND a.isActive = true")
    void deactivateExpiredSessions(@Param("now") Instant now);
}
