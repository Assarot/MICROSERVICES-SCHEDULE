package pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "category_resource")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResourceEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_category_resource")
    private Long idCategoryResource;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}
