package pe.edu.upeu.microservice_auth.domain.exception;

public class InvalidTokenException extends DomainException {
    
    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException() {
        super("Invalid or expired token");
    }
}
