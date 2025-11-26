package pe.edu.upeu.microservice_auth.infrastructure.adapter.input.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_auth.domain.model.AuthUser;
import pe.edu.upeu.microservice_auth.domain.port.input.UserManagementUseCase;
import pe.edu.upeu.microservice_auth.domain.port.input.RegisterUseCase;
import pe.edu.upeu.microservice_auth.infrastructure.adapter.input.dto.UserResponseDTO;
import pe.edu.upeu.microservice_auth.infrastructure.adapter.input.dto.UserRegisterDTO;
import pe.edu.upeu.microservice_auth.infrastructure.adapter.input.mapper.UserMapper;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/auth/users")
@RequiredArgsConstructor
public class UserController {

    private final UserManagementUseCase userManagementUseCase;
    private final RegisterUseCase registerUseCase;
    private final UserMapper userMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        log.info("GET /api/auth/users");

        List<AuthUser> users = userManagementUseCase.getAllUsers();
        List<UserResponseDTO> response = users.stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-profile/{profileId}")
    public ResponseEntity<UserResponseDTO> getUserByProfileId(@PathVariable Long profileId) {
        log.info("GET /api/auth/users/by-profile/{}", profileId);

        AuthUser user = userManagementUseCase.getUserByProfileId(profileId);
        UserResponseDTO response = userMapper.toResponseDTO(user);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRegisterDTO registerDTO) {
        log.info("POST /api/auth/users - create user: {}", registerDTO.getUsername());

        AuthUser user = registerUseCase.register(
                registerDTO.getUsername(),
                registerDTO.getPassword(),
                registerDTO.getUserProfileId()
        );

        UserResponseDTO response = userMapper.toResponseDTO(user);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        log.info("GET /api/auth/users/{}", id);

        AuthUser user = userManagementUseCase.getUserById(id);
        UserResponseDTO response = userMapper.toResponseDTO(user);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'COOROOMS', 'ASACAD')")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        log.info("PUT /api/auth/users/{}", id);

        AuthUser userToUpdate = AuthUser.builder()
                .username((String) updates.get("username"))
                .isActive((Boolean) updates.get("isActive"))
                .idUserProfile(updates.get("userProfileId") != null ? 
                        ((Number) updates.get("userProfileId")).longValue() : null)
                .build();

        AuthUser updatedUser = userManagementUseCase.updateUser(id, userToUpdate);
        UserResponseDTO response = userMapper.toResponseDTO(updatedUser);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        log.info("DELETE /api/auth/users/{}", id);

        userManagementUseCase.deleteUser(id);
        
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }

    @PostMapping("/{id}/roles/{roleName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> assignRole(
            @PathVariable Long id,
            @PathVariable String roleName) {
        log.info("POST /api/auth/users/{}/roles/{}", id, roleName);

        userManagementUseCase.assignRoleToUser(id, roleName);
        
        return ResponseEntity.ok(Map.of("message", "Role assigned successfully"));
    }

    @DeleteMapping("/{id}/roles/{roleName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> removeRole(
            @PathVariable Long id,
            @PathVariable String roleName) {
        log.info("DELETE /api/auth/users/{}/roles/{}", id, roleName);

        userManagementUseCase.removeRoleFromUser(id, roleName);
        
        return ResponseEntity.ok(Map.of("message", "Role removed successfully"));
    }
}
