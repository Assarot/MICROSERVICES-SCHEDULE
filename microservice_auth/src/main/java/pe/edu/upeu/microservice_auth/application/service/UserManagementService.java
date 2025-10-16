package pe.edu.upeu.microservice_auth.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.microservice_auth.domain.exception.RoleNotFoundException;
import pe.edu.upeu.microservice_auth.domain.exception.UserNotFoundException;
import pe.edu.upeu.microservice_auth.domain.model.AuthUser;
import pe.edu.upeu.microservice_auth.domain.model.Role;
import pe.edu.upeu.microservice_auth.domain.port.input.UserManagementUseCase;
import pe.edu.upeu.microservice_auth.domain.port.output.RoleRepositoryPort;
import pe.edu.upeu.microservice_auth.domain.port.output.UserRepositoryPort;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserManagementService implements UserManagementUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final RoleRepositoryPort roleRepositoryPort;

    @Override
    public AuthUser getUserById(Long id) {
        log.info("Fetching user by id: {}", id);
        return userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public List<AuthUser> getAllUsers() {
        log.info("Fetching all users");
        return userRepositoryPort.findAll();
    }

    @Override
    @Transactional
    public AuthUser updateUser(Long id, AuthUser user) {
        log.info("Updating user with id: {}", id);
        
        AuthUser existingUser = userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (user.getUsername() != null && !user.getUsername().equals(existingUser.getUsername())) {
            existingUser.setUsername(user.getUsername());
        }

        if (user.getIsActive() != null) {
            existingUser.setIsActive(user.getIsActive());
        }

        if (user.getIdUserProfile() != null) {
            existingUser.setIdUserProfile(user.getIdUserProfile());
        }

        return userRepositoryPort.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);
        
        if (!userRepositoryPort.findById(id).isPresent()) {
            throw new UserNotFoundException(id);
        }

        userRepositoryPort.deleteById(id);
    }

    @Override
    @Transactional
    public void assignRoleToUser(Long userId, String roleName) {
        log.info("Assigning role {} to user id: {}", roleName, userId);

        AuthUser user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Role role = roleRepositoryPort.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName));

        user.addRole(role);
        userRepositoryPort.save(user);
    }

    @Override
    @Transactional
    public void removeRoleFromUser(Long userId, String roleName) {
        log.info("Removing role {} from user id: {}", roleName, userId);

        AuthUser user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Role role = roleRepositoryPort.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName));

        user.removeRole(role);
        userRepositoryPort.save(user);
    }
}
