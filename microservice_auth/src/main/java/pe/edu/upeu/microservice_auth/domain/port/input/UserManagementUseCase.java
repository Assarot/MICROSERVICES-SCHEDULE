package pe.edu.upeu.microservice_auth.domain.port.input;

import pe.edu.upeu.microservice_auth.domain.model.AuthUser;

import java.util.List;

public interface UserManagementUseCase {
    
    AuthUser getUserById(Long id);
    
    AuthUser getUserByProfileId(Long profileId);
    
    List<AuthUser> getAllUsers();
    
    AuthUser updateUser(Long id, AuthUser user);
    
    void deleteUser(Long id);
    
    void assignRoleToUser(Long userId, String roleName);
    
    void removeRoleFromUser(Long userId, String roleName);
}
