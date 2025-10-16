package pe.edu.upeu.microservice_incident.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeverityDTO {
    private Long idSeverity;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotNull(message = "IsActive is required")
    private Boolean isActive;
}
