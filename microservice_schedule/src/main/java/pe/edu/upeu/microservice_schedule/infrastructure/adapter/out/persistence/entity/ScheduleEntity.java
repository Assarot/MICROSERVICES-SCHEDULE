package pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "schedule")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_schedule")
    private Long idSchedule;
    
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
    
    @Column(name = "duration")
    private Integer duration;
    
    @Column(name = "id_type_schedule")
    private Long idTypeSchedule;
    
    @Column(name = "id_academic_space")
    private Long idAcademicSpace;
    
    @Column(name = "id_course_assignment")
    private Long idCourseAssignment;
    
    @Column(name = "id_week_name")
    private Long idWeekName;
}
