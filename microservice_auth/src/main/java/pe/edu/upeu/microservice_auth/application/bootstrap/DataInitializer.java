package pe.edu.upeu.microservice_auth.application.bootstrap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.microservice_auth.domain.model.AuthUser;
import pe.edu.upeu.microservice_auth.domain.model.Role;
import pe.edu.upeu.microservice_auth.domain.port.output.PasswordEncoderPort;
import pe.edu.upeu.microservice_auth.domain.port.output.RoleRepositoryPort;
import pe.edu.upeu.microservice_auth.domain.port.output.UserRepositoryPort;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepositoryPort roleRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("[DataInitializer] Ensuring initial roles and users exist");

        // Ensure roles
        Role roleAsacad = ensureRole("ASACAD");
        Role roleCoorooms = ensureRole("COOROOMS");
        Role roleAdmin = ensureRole("ADMIN");

        // Ensure users with roles
        ensureUserWithRole("diana", "diana123", roleAsacad);
        ensureUserWithRole("pari", "pari123", roleCoorooms);
        ensureUserWithRole("admin", "admin123", roleAdmin);

        log.info("[DataInitializer] Initial data ready");
    }

    private Role ensureRole(String name) {
        Optional<Role> existing = roleRepositoryPort.findByName(name);
        if (existing.isPresent()) {
            Role role = existing.get();
            if (role.getIsActive() == null || !role.getIsActive()) {
                role.setIsActive(true);
                roleRepositoryPort.save(role);
            }
            return role;
        }
        Role role = Role.builder().name(name).isActive(true).build();
        return roleRepositoryPort.save(role);
    }

    private void ensureUserWithRole(String username, String rawPassword, Role role) {
        Optional<AuthUser> existing = userRepositoryPort.findByUsername(username);
        if (existing.isPresent()) {
            AuthUser user = existing.get();
            boolean hasRole = user.getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase(role.getName()));
            if (!hasRole) {
                user.addRole(role);
                userRepositoryPort.save(user);
                log.info("[DataInitializer] Added role {} to existing user {}", role.getName(), username);
            }
            return;
        }
        String encoded = passwordEncoderPort.encode(rawPassword);
        AuthUser user = AuthUser.builder()
                .username(username)
                .password(encoded)
                .isActive(true)
                .build();
        user.addRole(role);
        userRepositoryPort.save(user);
        log.info("[DataInitializer] Created user {} with role {}", username, role.getName());
    }
}
