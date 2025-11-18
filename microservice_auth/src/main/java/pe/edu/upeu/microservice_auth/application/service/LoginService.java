package pe.edu.upeu.microservice_auth.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.microservice_auth.domain.model.AuthUser;
import pe.edu.upeu.microservice_auth.domain.port.input.LoginUseCase;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

    private final AuthService authService;

    @Override
    @Transactional
    public Map<String, Object> login(String username, String password) {
        log.info("Login attempt for user: {}", username);
        return authService.login(username, password);
    }

    @Override
    public AuthUser getUserByUsername(String username) {
        return authService.getUserByUsername(username);
    }
}
