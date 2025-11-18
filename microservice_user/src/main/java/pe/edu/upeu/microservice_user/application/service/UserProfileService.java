package pe.edu.upeu.microservice_user.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.microservice_user.domain.exception.EmailAlreadyExistsException;
import pe.edu.upeu.microservice_user.domain.exception.InvalidUserProfileException;
import pe.edu.upeu.microservice_user.domain.exception.UserProfileNotFoundException;
import pe.edu.upeu.microservice_user.domain.model.UserProfile;
import pe.edu.upeu.microservice_user.domain.port.in.UserProfileServicePort;
import pe.edu.upeu.microservice_user.domain.port.out.UserProfileRepositoryPort;
import pe.edu.upeu.microservice_user.infrastructure.storage.CloudinaryService;
import pe.edu.upeu.microservice_user.infrastructure.client.AuthUserClient;
import pe.edu.upeu.microservice_user.infrastructure.client.dto.AuthUserResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de UserProfile
 * Contiene la lógica de negocio de la aplicación
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService implements UserProfileServicePort {
    
    private final UserProfileRepositoryPort userProfileRepository;
    private final CloudinaryService cloudinaryService;
    private final AuthUserClient authUserClient;

    @Value("${cloudinary.default_profile_picture}")
    private String defaultProfilePicture;
    
    @Override
    @Transactional
    public UserProfile createUserProfile(UserProfile userProfile) {
        log.info("Creando nuevo perfil de usuario con email: {}", userProfile.getEmail());
        
        // Validar que el email no exista
        if (userProfileRepository.existsByEmail(userProfile.getEmail())) {
            log.error("El email {} ya está registrado", userProfile.getEmail());
            throw new EmailAlreadyExistsException(userProfile.getEmail(), true);
        }
        
        // Validar formato de email
        if (!userProfile.isEmailValid()) {
            log.error("El email {} no es válido", userProfile.getEmail());
            throw new InvalidUserProfileException("El formato del email no es válido");
        }
        
        // Establecer timestamps
        LocalDateTime now = LocalDateTime.now();
        userProfile.setCreatedAt(now);
        userProfile.setUpdatedAt(now);

        // Establecer activo por defecto si no se especifica
        if (userProfile.getIsActive() == null) {
            userProfile.setIsActive(true);
        }

        // Establecer foto de perfil por defecto siempre en creación
        userProfile.setProfilePicture(defaultProfilePicture);
        
        UserProfile savedProfile = userProfileRepository.save(userProfile);
        log.info("Perfil de usuario creado exitosamente con ID: {}", savedProfile.getIdUserProfile());
        
        return savedProfile;
    }
    
    @Override
    @Transactional
    public UserProfile updateUserProfile(Long id, UserProfile userProfile) {
        log.info("Actualizando perfil de usuario con ID: {}", id);
        
        // Verificar que el perfil existe
        UserProfile existingProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Perfil de usuario no encontrado con ID: {}", id);
                    return new UserProfileNotFoundException(id);
                });
        
        // Validar que el email no exista en otro perfil
        if (!existingProfile.getEmail().equals(userProfile.getEmail())) {
            if (userProfileRepository.existsByEmailAndIdNot(userProfile.getEmail(), id)) {
                log.error("El email {} ya está registrado en otro perfil", userProfile.getEmail());
                throw new EmailAlreadyExistsException(userProfile.getEmail(), true);
            }
        }
        
        // Validar formato de email
        if (!userProfile.isEmailValid()) {
            log.error("El email {} no es válido", userProfile.getEmail());
            throw new InvalidUserProfileException("El formato del email no es válido");
        }
        
        // Actualizar campos
        existingProfile.setNames(userProfile.getNames());
        existingProfile.setLastName(userProfile.getLastName());
        existingProfile.setEmail(userProfile.getEmail());
        existingProfile.setPhoneNumber(userProfile.getPhoneNumber());
        existingProfile.setAddress(userProfile.getAddress());
        existingProfile.setDob(userProfile.getDob());
        // Nota: profilePicture e isActive no se actualizan en este endpoint general
        existingProfile.updateTimestamp();
        
        UserProfile updatedProfile = userProfileRepository.save(existingProfile);
        log.info("Perfil de usuario actualizado exitosamente con ID: {}", id);
        
        return updatedProfile;
    }

    @Override
    @Transactional
    public UserProfile updateProfilePicture(Long id, MultipartFile file) {
        log.info("Actualizando foto de perfil para usuario con ID: {}", id);

        UserProfile existingProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Perfil de usuario no encontrado con ID: {}", id);
                    return new UserProfileNotFoundException(id);
                });

        try {
            String url = cloudinaryService.uploadImage(file, "env-profile-pics");
            existingProfile.setProfilePicture(url);
            existingProfile.updateTimestamp();
            UserProfile updated = userProfileRepository.save(existingProfile);
            log.info("Foto de perfil actualizada exitosamente para ID: {}", id);
            return updated;
        } catch (Exception ex) {
            log.error("Error subiendo imagen a Cloudinary para ID {}: {}", id, ex.getMessage(), ex);
            throw new InvalidUserProfileException("No se pudo subir la imagen de perfil");
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<UserProfile> getUserProfileById(Long id) {
        log.info("Consultando perfil de usuario con ID: {}", id);
        return userProfileRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<UserProfile> getUserProfileByEmail(String email) {
        log.info("Consultando perfil de usuario con email: {}", email);
        return userProfileRepository.findByEmail(email);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserProfile> getAllActiveUserProfiles() {
        log.info("Consultando todos los perfiles activos");
        return userProfileRepository.findAllActive();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserProfile> getAllUserProfiles() {
        log.info("Consultando todos los perfiles");
        return userProfileRepository.findAll();
    }
    
    @Override
    @Transactional
    public void deleteUserProfile(Long id) {
        log.info("Eliminando perfil de usuario con ID: {}", id);
        
        if (!userProfileRepository.findById(id).isPresent()) {
            log.error("Perfil de usuario no encontrado con ID: {}", id);
            throw new UserProfileNotFoundException(id);
        }
        
        userProfileRepository.deleteById(id);
        log.info("Perfil de usuario eliminado exitosamente con ID: {}", id);
    }
    
    @Override
    @Transactional
    public void deactivateUserProfile(Long id) {
        log.info("Desactivando perfil de usuario con ID: {}", id);
        
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Perfil de usuario no encontrado con ID: {}", id);
                    return new UserProfileNotFoundException(id);
                });
        
        userProfile.deactivate();
        userProfileRepository.save(userProfile);
        log.info("Perfil de usuario desactivado exitosamente con ID: {}", id);
    }
    
    @Override
    @Transactional
    public void activateUserProfile(Long id) {
        log.info("Activando perfil de usuario con ID: {}", id);
        
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Perfil de usuario no encontrado con ID: {}", id);
                    return new UserProfileNotFoundException(id);
                });
        
        userProfile.activate();
        userProfileRepository.save(userProfile);
        log.info("Perfil de usuario activado exitosamente con ID: {}", id);
    }

    @Transactional(readOnly = true)
    public AuthUserResponseDto getAuthUserByProfileId(Long profileId) {
        log.info("Obteniendo auth_user desde microservice_auth por profileId: {}", profileId);
        return authUserClient.getUserByProfileId(profileId);
    }
}
