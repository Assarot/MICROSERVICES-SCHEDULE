package pe.edu.upeu.microservice_user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad de dominio UserProfile
 * Representa el perfil de usuario del sistema
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    
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
    
    /**
     * Activa el perfil de usuario
     */
    public void activate() {
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Desactiva el perfil de usuario
     */
    public void deactivate() {
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Actualiza la marca de tiempo de modificación
     */
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Valida que el email tenga formato correcto
     */
    public boolean isEmailValid() {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}
