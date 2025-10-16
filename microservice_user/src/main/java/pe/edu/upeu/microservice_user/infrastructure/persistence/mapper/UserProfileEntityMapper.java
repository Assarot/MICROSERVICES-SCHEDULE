package pe.edu.upeu.microservice_user.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_user.domain.model.UserProfile;
import pe.edu.upeu.microservice_user.infrastructure.persistence.entity.UserProfileEntity;

/**
 * Mapper para convertir entre entidades JPA y entidades de dominio
 */
@Component
public class UserProfileEntityMapper {
    
    /**
     * Convierte de entidad de dominio a entidad JPA
     */
    public UserProfileEntity toEntity(UserProfile domain) {
        if (domain == null) {
            return null;
        }
        
        return UserProfileEntity.builder()
                .idUserProfile(domain.getIdUserProfile())
                .names(domain.getNames())
                .lastName(domain.getLastName())
                .email(domain.getEmail())
                .phoneNumber(domain.getPhoneNumber())
                .address(domain.getAddress())
                .dob(domain.getDob())
                .profilePicture(domain.getProfilePicture())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .isActive(domain.getIsActive())
                .build();
    }
    
    /**
     * Convierte de entidad JPA a entidad de dominio
     */
    public UserProfile toDomain(UserProfileEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return UserProfile.builder()
                .idUserProfile(entity.getIdUserProfile())
                .names(entity.getNames())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .address(entity.getAddress())
                .dob(entity.getDob())
                .profilePicture(entity.getProfilePicture())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .isActive(entity.getIsActive())
                .build();
    }
}
