package pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "week_day")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeekDayEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_schedule")
    private Long idSchedule;
    
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
