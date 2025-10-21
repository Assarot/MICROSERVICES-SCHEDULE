package pe.edu.upeu.microservice_auth.domain.exception;

public class UserAlreadyExistsException extends DomainException {
    
    public UserAlreadyExistsException(String username) {
        super("User already exists with username: " + username);
    }
}
