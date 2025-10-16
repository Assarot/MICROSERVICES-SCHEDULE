package pe.edu.upeu.microservice_incident.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidentAttachmentDTO {
    private Long idIncidentAttachment;
    
    private String photoUrl;
    
    private LocalDateTime uploadedAt;
    
    @NotNull(message = "Incident ID is required")
    private Long idIncident;
}
