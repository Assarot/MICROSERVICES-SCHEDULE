package pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleAssignmentRequest {
    
    @NotNull(message = "Assignment date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate assignmentDate;
    
    private Long idSchedule;
    private Long idTypeAssignment;
    private Long idUserProfile;
    
    @NotNull(message = "isActive is required")
    private Boolean isActive;
}
