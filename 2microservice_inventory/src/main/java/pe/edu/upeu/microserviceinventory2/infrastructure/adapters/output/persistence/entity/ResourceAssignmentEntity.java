package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resource_assignment")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceAssignmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resource_assignment")
    private Long idResourceAssignment;

    @Column(name = "id_academic_space_fk")
    private Long idAcademicSpace;

    @Column(name = "id_resource_fk")
    private Long idResource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_resource_fk", insertable = false, updatable = false)
    private ResourceEntity resource;
}
