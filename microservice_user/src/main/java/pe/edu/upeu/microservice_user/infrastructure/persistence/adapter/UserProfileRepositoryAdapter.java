package pe.edu.upeu.microservice_user.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_user.domain.model.UserProfile;
import pe.edu.upeu.microservice_user.domain.port.out.UserProfileRepositoryPort;
import pe.edu.upeu.microservice_user.infrastructure.persistence.entity.UserProfileEntity;
import pe.edu.upeu.microservice_user.infrastructure.persistence.mapper.UserProfileEntityMapper;
import pe.edu.upeu.microservice_user.infrastructure.persistence.repository.JpaUserProfileRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador de persistencia para UserProfile
 * Implementa el puerto de salida usando JPA
 */
@Component
@RequiredArgsConstructor
public class UserProfileRepositoryAdapter implements UserProfileRepositoryPort {
    
    private final JpaUserProfileRepository jpaRepository;
    private final UserProfileEntityMapper mapper;
    
    @Override
    public UserProfile save(UserProfile userProfile) {
        UserProfileEntity entity = mapper.toEntity(userProfile);
        UserProfileEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<UserProfile> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public Optional<UserProfile> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(mapper::toDomain);
    }
    
    @Override
    public List<UserProfile> findAllActive() {
        return jpaRepository.findByIsActiveTrue().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<UserProfile> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
    
    @Override
    public boolean existsByEmailAndIdNot(String email, Long excludeId) {
        return jpaRepository.existsByEmailAndIdNot(email, excludeId);
    }
}
