package pe.edu.upeu.microservice_auth.domain.exception;

public class RoleNotFoundException extends DomainException {
    
    public RoleNotFoundException(String roleName) {
        super("Role not found: " + roleName);
    }

    public RoleNotFoundException(Long id) {
        super("Role not found with id: " + id);
    }
}
