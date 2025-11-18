package pe.edu.upeu.microservice_auth.infrastructure.adapter.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_auth.domain.model.AuthUser;
import pe.edu.upeu.microservice_auth.domain.port.output.UserRepositoryPort;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;

    @Override
    public AuthUser save(AuthUser user) {
        return userJpaRepository.save(user);
    }

    @Override
    public Optional<AuthUser> findById(Long id) {
        return userJpaRepository.findById(id);
    }

    @Override
    public Optional<AuthUser> findByUsername(String username) {
        return userJpaRepository.findByUsername(username);
    }

    @Override
    public Optional<AuthUser> findByUserProfileId(Long userProfileId) {
        return userJpaRepository.findByIdUserProfile(userProfileId);
    }

    @Override
    public List<AuthUser> findAll() {
        return userJpaRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        userJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }
}
