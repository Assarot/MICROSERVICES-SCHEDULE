package pe.edu.upeu.microservice_user.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.microservice_user.infrastructure.persistence.entity.UserProfileEntity;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para UserProfile
 */
@Repository
public interface JpaUserProfileRepository extends JpaRepository<UserProfileEntity, Long> {
    
    /**
     * Busca un perfil por email
     */
    Optional<UserProfileEntity> findByEmail(String email);
    
    /**
     * Lista todos los perfiles activos
     */
    List<UserProfileEntity> findByIsActiveTrue();
    
    /**
     * Verifica si existe un perfil con el email dado
     */
    boolean existsByEmail(String email);
    
    /**
     * Verifica si existe un perfil con el email dado excluyendo un ID
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserProfileEntity u WHERE u.email = :email AND u.idUserProfile != :id")
    boolean existsByEmailAndIdNot(@Param("email") String email, @Param("id") Long id);
}
