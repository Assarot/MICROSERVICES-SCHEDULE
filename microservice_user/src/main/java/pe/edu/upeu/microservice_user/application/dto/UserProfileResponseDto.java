package pe.edu.upeu.microservice_user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para enviar datos de UserProfile en respuestas
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseDto {
    
    private Long idUserProfile;
    private String names;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDate dob;
    private String profilePicture;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
}
