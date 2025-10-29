package pe.edu.upeu.microserviceinventory2.domain.exception;

public class StateNotFoundException extends RuntimeException {
    public StateNotFoundException(Long id) {
        super("State not found with id=" + id);
    }
}
