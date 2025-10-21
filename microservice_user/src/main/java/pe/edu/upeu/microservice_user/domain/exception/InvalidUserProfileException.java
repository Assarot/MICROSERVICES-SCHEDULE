package pe.edu.upeu.microservice_user.domain.exception;

/**
 * Excepción para validaciones de negocio del perfil de usuario
 */
public class InvalidUserProfileException extends RuntimeException {
    
    public InvalidUserProfileException(String message) {
        super(message);
    }
}
