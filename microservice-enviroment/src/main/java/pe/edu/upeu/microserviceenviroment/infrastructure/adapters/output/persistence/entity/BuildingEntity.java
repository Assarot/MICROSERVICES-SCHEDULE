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
@Builder
@Table(name = "building")
public class BuildingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_building")
    private Long idBuilding;
    @Column(name = "name")
    private String name;
    @Column(name = "is_active")
    private char isActive;


    @OneToMany(mappedBy = "buildingEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<FloorEntity> floorEntities;
}
