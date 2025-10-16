package pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "type_hour")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypeHourEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_hour")
    private Long idTypeHour;
    
    @Column(name = "type_hour", nullable = false, unique = true)
    private String typeHour;
}
