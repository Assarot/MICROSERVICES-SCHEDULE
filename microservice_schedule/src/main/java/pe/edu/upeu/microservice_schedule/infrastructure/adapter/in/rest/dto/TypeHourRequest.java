package pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypeHourRequest {
    
    @NotBlank(message = "Type hour is required")
    private String typeHour;
}
