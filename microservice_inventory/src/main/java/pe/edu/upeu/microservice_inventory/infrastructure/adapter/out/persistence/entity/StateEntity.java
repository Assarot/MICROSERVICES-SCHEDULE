package pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "state")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StateEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_state")
    private Long idState;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}
