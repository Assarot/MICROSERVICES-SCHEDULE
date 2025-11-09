package pe.edu.upeu.microservice_auth.infrastructure.adapter.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.microservice_auth.domain.model.RefreshToken;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {
    
    Optional<RefreshToken> findByRefreshToken(String token);

    @Query("SELECT rt FROM RefreshToken rt WHERE rt.refreshToken = :token AND rt.isActive = true")
    Optional<RefreshToken> findActiveByRefreshToken(@Param("token") String token);
    
    void deleteByAuthUser_IdAuthUser(Long userId);
    
    void deleteByRefreshToken(String token);
    
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiryDate < :now")
    void deleteExpiredTokens(@Param("now") Instant now);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.isActive = false, rt.revokedAt = :now WHERE rt.refreshToken = :token AND rt.isActive = true")
    void deactivateByRefreshToken(@Param("token") String token, @Param("now") Instant now);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.isActive = false, rt.revokedAt = :now WHERE rt.expiryDate < :now AND rt.isActive = true")
    void deactivateExpiredTokens(@Param("now") Instant now);
}
