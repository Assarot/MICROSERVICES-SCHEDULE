package pe.edu.upeu.microservice_user.application.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_user.application.dto.UserProfileRequestDto;
import pe.edu.upeu.microservice_user.application.dto.UserProfileResponseDto;
import pe.edu.upeu.microservice_user.application.dto.UserProfileUpdateRequestDto;
import pe.edu.upeu.microservice_user.domain.model.UserProfile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre DTOs y entidades de dominio
 */
@Component
public class UserProfileMapper {
    
    /**
     * Convierte de RequestDto a entidad de dominio
     */
    public UserProfile toDomain(UserProfileRequestDto dto) {
        if (dto == null) {
            return null;
        }
        
        return UserProfile.builder()
                .names(dto.getNames())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .address(dto.getAddress())
                .dob(dto.getDob())
                .profilePicture(dto.getProfilePicture())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Convierte de UpdateRequestDto a entidad de dominio parcial para actualización
     */
    public UserProfile toDomain(UserProfileUpdateRequestDto dto) {
        if (dto == null) {
            return null;
        }

        return UserProfile.builder()
                .names(dto.getNames())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .address(dto.getAddress())
                .dob(dto.getDob())
                .build();
    }
    
    /**
     * Convierte de entidad de dominio a ResponseDto
     */
    public UserProfileResponseDto toResponseDto(UserProfile userProfile) {
        if (userProfile == null) {
            return null;
        }
        
        return UserProfileResponseDto.builder()
                .idUserProfile(userProfile.getIdUserProfile())
                .names(userProfile.getNames())
                .lastName(userProfile.getLastName())
                .email(userProfile.getEmail())
                .phoneNumber(userProfile.getPhoneNumber())
                .address(userProfile.getAddress())
                .dob(userProfile.getDob())
                .profilePicture(userProfile.getProfilePicture())
                .createdAt(userProfile.getCreatedAt())
                .updatedAt(userProfile.getUpdatedAt())
                .isActive(userProfile.getIsActive())
                .build();
    }
    
    /**
     * Convierte una lista de entidades a lista de ResponseDto
     */
    public List<UserProfileResponseDto> toResponseDtoList(List<UserProfile> userProfiles) {
        if (userProfiles == null) {
            return null;
        }
        
        return userProfiles.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Actualiza una entidad existente con datos del RequestDto
     */
    public void updateDomainFromDto(UserProfile userProfile, UserProfileRequestDto dto) {
        if (userProfile == null || dto == null) {
            return;
        }
        
        userProfile.setNames(dto.getNames());
        userProfile.setLastName(dto.getLastName());
        userProfile.setEmail(dto.getEmail());
        userProfile.setPhoneNumber(dto.getPhoneNumber());
        userProfile.setAddress(dto.getAddress());
        userProfile.setDob(dto.getDob());
        userProfile.setProfilePicture(dto.getProfilePicture());
        userProfile.setIsActive(dto.getIsActive());
        userProfile.setUpdatedAt(LocalDateTime.now());
    }

    /**
     * Actualiza una entidad existente con datos del UpdateRequestDto sin modificar isActive ni profilePicture
     */
    public void updateDomainFromUpdateDto(UserProfile userProfile, UserProfileUpdateRequestDto dto) {
        if (userProfile == null || dto == null) {
            return;
        }

        userProfile.setNames(dto.getNames());
        userProfile.setLastName(dto.getLastName());
        userProfile.setEmail(dto.getEmail());
        userProfile.setPhoneNumber(dto.getPhoneNumber());
        userProfile.setAddress(dto.getAddress());
        userProfile.setDob(dto.getDob());
        userProfile.setUpdatedAt(LocalDateTime.now());
    }
}
