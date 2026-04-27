package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

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
