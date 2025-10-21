package pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resource")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resource")
    private Long idResource;
    
    @Column(name = "code", nullable = false, length = 50)
    private String code;
    
    @Column(name = "stock")
    private Integer stock;
    
    @Column(name = "resource_photo_url", length = 500)
    private String resourcePhotoUrl;
    
    @Column(name = "observation", columnDefinition = "TEXT")
    private String observation;
    
    @Column(name = "id_resource_type_fk")
    private Long idResourceType;
    
    @Column(name = "id_state_fk")
    private Long idState;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_resource_type_fk", insertable = false, updatable = false)
    private ResourceTypeEntity resourceType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_state_fk", insertable = false, updatable = false)
    private StateEntity state;
}
