package pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeekDayRequest {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotNull(message = "isActive is required")
    private Boolean isActive;
}
