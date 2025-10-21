package pe.edu.upeu.microservice_auth.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upeu.microservice_auth.domain.exception.UserAlreadyExistsException;
import pe.edu.upeu.microservice_auth.domain.model.AuthUser;
import pe.edu.upeu.microservice_auth.domain.model.Role;
import pe.edu.upeu.microservice_auth.domain.port.output.PasswordEncoderPort;
import pe.edu.upeu.microservice_auth.domain.port.output.RoleRepositoryPort;
import pe.edu.upeu.microservice_auth.domain.port.output.UserRepositoryPort;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private RoleRepositoryPort roleRepositoryPort;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @InjectMocks
    private RegisterService registerService;

    private Role userRole;

    @BeforeEach
    void setUp() {
        userRole = Role.builder()
                .idRole(1L)
                .name("USER")
                .isActive(true)
                .build();
    }

    @Test
    void register_withValidData_shouldCreateUser() {
        // Arrange
        String username = "newuser";
        String password = "password123";
        Long userProfileId = 1L;
        String encodedPassword = "encodedPassword";

        when(userRepositoryPort.existsByUsername(username)).thenReturn(false);
        when(passwordEncoderPort.encode(password)).thenReturn(encodedPassword);
        when(roleRepositoryPort.findByName("USER")).thenReturn(Optional.of(userRole));
        when(userRepositoryPort.save(any(AuthUser.class))).thenAnswer(invocation -> {
            AuthUser user = invocation.getArgument(0);
            user.setIdAuthUser(1L);
            return user;
        });

        // Act
        AuthUser result = registerService.register(username, password, userProfileId);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(encodedPassword, result.getPassword());
        assertTrue(result.getIsActive());
        assertEquals(0, result.getFailedLoginsAttempts());

        verify(userRepositoryPort).existsByUsername(username);
        verify(passwordEncoderPort).encode(password);
        verify(userRepositoryPort).save(any(AuthUser.class));
    }

    @Test
    void register_withExistingUsername_shouldThrowException() {
        // Arrange
        String username = "existinguser";
        String password = "password123";

        when(userRepositoryPort.existsByUsername(username)).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> {
            registerService.register(username, password, null);
        });

        verify(userRepositoryPort).existsByUsername(username);
        verify(passwordEncoderPort, never()).encode(anyString());
        verify(userRepositoryPort, never()).save(any(AuthUser.class));
    }
}
