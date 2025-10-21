package pe.edu.upeu.microservice_auth.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.microservice_auth.domain.exception.UserAlreadyExistsException;
import pe.edu.upeu.microservice_auth.domain.model.AuthUser;
import pe.edu.upeu.microservice_auth.domain.port.input.RegisterUseCase;
import pe.edu.upeu.microservice_auth.domain.port.output.PasswordEncoderPort;
import pe.edu.upeu.microservice_auth.domain.port.output.RoleRepositoryPort;
import pe.edu.upeu.microservice_auth.domain.port.output.UserRepositoryPort;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterService implements RegisterUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final RoleRepositoryPort roleRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    @Override
    @Transactional
    public AuthUser register(String username, String password, Long userProfileId) {
        log.info("Attempting to register user: {}", username);

        if (userRepositoryPort.existsByUsername(username)) {
            log.warn("User already exists: {}", username);
            throw new UserAlreadyExistsException(username);
        }

        String encodedPassword = passwordEncoderPort.encode(password);

        AuthUser newUser = AuthUser.builder()
                .username(username)
                .password(encodedPassword)
                .idUserProfile(userProfileId)
                .isActive(true)
                .failedLoginsAttempts(0)
                .build();

        // Assign default role (USER) if exists
        roleRepositoryPort.findByName("USER").ifPresent(newUser::addRole);

        AuthUser savedUser = userRepositoryPort.save(newUser);
        log.info("User registered successfully: {}", username);

        return savedUser;
    }
}
