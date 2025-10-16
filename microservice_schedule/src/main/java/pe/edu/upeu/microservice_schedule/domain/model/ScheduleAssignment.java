package pe.edu.upeu.microservice_schedule.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleAssignment {
    private Long idScheduleAssignment;
    private LocalDate assignmentDate;
    private Long idSchedule;
    private Long idTypeAssignment;
    private Long idUserProfile;
    private Boolean isActive;
}
