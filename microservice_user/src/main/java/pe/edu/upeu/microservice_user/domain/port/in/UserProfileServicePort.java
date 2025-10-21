package pe.edu.upeu.microservice_user.domain.port.in;

import pe.edu.upeu.microservice_user.domain.model.UserProfile;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de entrada para el servicio de UserProfile
 * Define los casos de uso disponibles
 */
public interface UserProfileServicePort {
    
    /**
     * Crea un nuevo perfil de usuario
     * @param userProfile datos del perfil
     * @return perfil creado
     */
    UserProfile createUserProfile(UserProfile userProfile);
    
    /**
     * Actualiza un perfil existente
     * @param id identificador del perfil
     * @param userProfile datos actualizados
     * @return perfil actualizado
     */
    UserProfile updateUserProfile(Long id, UserProfile userProfile);
    
    /**
     * Obtiene un perfil por su ID
     * @param id identificador del perfil
     * @return Optional con el perfil si existe
     */
    Optional<UserProfile> getUserProfileById(Long id);
    
    /**
     * Obtiene un perfil por email
     * @param email email del usuario
     * @return Optional con el perfil si existe
     */
    Optional<UserProfile> getUserProfileByEmail(String email);
    
    /**
     * Lista todos los perfiles activos
     * @return lista de perfiles activos
     */
    List<UserProfile> getAllActiveUserProfiles();
    
    /**
     * Lista todos los perfiles
     * @return lista de todos los perfiles
     */
    List<UserProfile> getAllUserProfiles();
    
    /**
     * Elimina un perfil (eliminación física)
     * @param id identificador del perfil
     */
    void deleteUserProfile(Long id);
    
    /**
     * Desactiva un perfil (eliminación lógica)
     * @param id identificador del perfil
     */
    void deactivateUserProfile(Long id);
    
    /**
     * Activa un perfil
     * @param id identificador del perfil
     */
    void activateUserProfile(Long id);
}
