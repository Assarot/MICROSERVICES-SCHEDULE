package pe.edu.upeu.microservice_incident.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "severity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Severity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_severity")
    private Long idSeverity;
    
    @Column(nullable = false, unique = true, length = 100)
    private String name;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @OneToMany(mappedBy = "severity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Incident> incidents;
}
