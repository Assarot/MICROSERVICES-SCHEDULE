package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "academic_space")
public class AcademicSpaceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_academic_space")
    private Long idAcademicSpace;
    @Column(name = "space_name")
    private String spaceName;
    @Column(name = "observation")
    private String observation;
    @Column(name = "location")
    private String location;
    @Column(name = "capacity")
    private int capacity;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_academic_space", nullable = false)
    private TypeAcademicSpaceEntity typeAcademicSpaceEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_state", nullable = false)
    private StateEntity stateEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_floor", nullable = false)
    private FloorEntity floorEntity;
}
