package pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "schedule_assignment")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleAssignmentEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_schedule_assignment")
    private Long idScheduleAssignment;
    
    @Column(name = "assignment_date", nullable = false)
    private LocalDate assignmentDate;
    
    @Column(name = "id_schedule")
    private Long idSchedule;
    
    @Column(name = "id_type_assignment")
    private Long idTypeAssignment;
    
    @Column(name = "id_user_profile")
    private Long idUserProfile;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
