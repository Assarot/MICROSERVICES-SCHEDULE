package pe.edu.upeu.microservice_auth.domain.exception;

public class InvalidCredentialsException extends DomainException {
    
    public InvalidCredentialsException() {
        super("Invalid username or password");
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
