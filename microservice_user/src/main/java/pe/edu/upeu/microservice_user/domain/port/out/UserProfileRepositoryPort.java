package pe.edu.upeu.microservice_user.domain.port.out;

import pe.edu.upeu.microservice_user.domain.model.UserProfile;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para el repositorio de UserProfile
 * Define las operaciones de persistencia necesarias
 */
public interface UserProfileRepositoryPort {
    
    /**
     * Guarda un perfil de usuario
     * @param userProfile perfil a guardar
     * @return perfil guardado
     */
    UserProfile save(UserProfile userProfile);
    
    /**
     * Busca un perfil por su ID
     * @param id identificador del perfil
     * @return Optional con el perfil si existe
     */
    Optional<UserProfile> findById(Long id);
    
    /**
     * Busca un perfil por email
     * @param email email del usuario
     * @return Optional con el perfil si existe
     */
    Optional<UserProfile> findByEmail(String email);
    
    /**
     * Lista todos los perfiles activos
     * @return lista de perfiles activos
     */
    List<UserProfile> findAllActive();
    
    /**
     * Lista todos los perfiles
     * @return lista de todos los perfiles
     */
    List<UserProfile> findAll();
    
    /**
     * Elimina un perfil por su ID
     * @param id identificador del perfil
     */
    void deleteById(Long id);
    
    /**
     * Verifica si existe un perfil con el email dado
     * @param email email a verificar
     * @return true si existe
     */
    boolean existsByEmail(String email);
    
    /**
     * Verifica si existe un perfil con el email dado excluyendo un ID
     * @param email email a verificar
     * @param excludeId ID a excluir
     * @return true si existe
     */
    boolean existsByEmailAndIdNot(String email, Long excludeId);
}
