package pe.edu.upeu.microserviceinventory2.domain.exception;

public class CategoryResourceNotFoundException extends RuntimeException {
    public CategoryResourceNotFoundException(Long id) {
        super("CategoryResource not found with id=" + id);
    }
}
