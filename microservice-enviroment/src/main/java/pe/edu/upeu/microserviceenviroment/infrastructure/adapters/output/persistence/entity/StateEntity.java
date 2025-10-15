package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "state")
public class StateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_state")
    private long idState;
    @Column(name = "name")
    private String name;
    @Column(name = "is_active")
    private char isActive;


    @OneToMany(mappedBy = "stateEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<AcademicSpaceEntity> academicSpaceEntities;
}
