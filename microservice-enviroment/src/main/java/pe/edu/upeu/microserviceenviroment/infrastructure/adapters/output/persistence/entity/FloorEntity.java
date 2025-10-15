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
@Table(name = "floor")
public class FloorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_floor")
    private Long idFloor;
    @Column(name = "floor_number")
    private int floorNumber;
    @Column(name = "is_active")
    private char isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_building", nullable = false)
    private BuildingEntity buildingEntity;

    @OneToMany(mappedBy = "floorEntity", cascade  = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<AcademicSpaceEntity> academicSpaceEntities;
}
