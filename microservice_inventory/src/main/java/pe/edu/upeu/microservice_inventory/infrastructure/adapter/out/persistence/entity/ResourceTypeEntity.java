package pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resource_type")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceTypeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resource_type")
    private Long idResourceType;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    
    @Column(name = "id_category_resource_fk")
    private Long idCategoryResource;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category_resource_fk", insertable = false, updatable = false)
    private CategoryResourceEntity categoryResource;
}
