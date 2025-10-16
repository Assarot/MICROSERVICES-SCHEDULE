package pe.edu.upeu.microservice_incident.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidentDTO {
    private Long idIncident;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    @NotNull(message = "Report date is required")
    private LocalDateTime reportDate;
    
    private LocalDateTime resolutionDate;
    
    private Long idReportedBy;
    
    private Long idResolvedBy;
    
    private Long idAcademicSpace;
    
    private Long idResource;
    
    @NotNull(message = "Severity ID is required")
    private Long idSeverity;
    
    @NotNull(message = "Status ID is required")
    private Long idStatus;
    
    private List<IncidentAttachmentDTO> attachments;
}
