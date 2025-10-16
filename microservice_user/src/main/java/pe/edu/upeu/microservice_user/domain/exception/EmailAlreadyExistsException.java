package pe.edu.upeu.microservice_user.domain.exception;

/**
 * Excepción cuando el email ya existe en el sistema
 */
public class EmailAlreadyExistsException extends RuntimeException {
    
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
    
    public EmailAlreadyExistsException(String email, boolean isEmail) {
        super("El email '" + email + "' ya está registrado en el sistema");
    }
}
