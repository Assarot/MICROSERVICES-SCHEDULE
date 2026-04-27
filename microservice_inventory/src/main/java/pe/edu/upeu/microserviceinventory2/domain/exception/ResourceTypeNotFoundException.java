package pe.edu.upeu.microserviceinventory2.domain.exception;

public class ResourceTypeNotFoundException extends RuntimeException {
    public ResourceTypeNotFoundException(Long id) {
        super("ResourceType not found with id=" + id);
    }
}
