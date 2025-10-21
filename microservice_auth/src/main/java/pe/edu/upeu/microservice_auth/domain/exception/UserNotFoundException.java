package pe.edu.upeu.microservice_auth.domain.exception;

public class UserNotFoundException extends DomainException {
    
    public UserNotFoundException(String username) {
        super("User not found with username: " + username);
    }

    public UserNotFoundException(Long id) {
        super("User not found with id: " + id);
    }
}
