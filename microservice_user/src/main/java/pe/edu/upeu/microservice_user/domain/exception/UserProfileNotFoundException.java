package pe.edu.upeu.microservice_user.domain.exception;

/**
 * Excepción cuando no se encuentra un perfil de usuario
 */
public class UserProfileNotFoundException extends RuntimeException {
    
    public UserProfileNotFoundException(String message) {
        super(message);
    }
    
    public UserProfileNotFoundException(Long id) {
        super("UserProfile no encontrado con ID: " + id);
    }
}
