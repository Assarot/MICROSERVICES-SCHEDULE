package pe.edu.upeu.microservice_incident.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "incident_attachment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidentAttachment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_incident_attachment")
    private Long idIncidentAttachment;
    
    @Column(name = "photo_url", nullable = false)
    private String photoUrl;
    
    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_incident", nullable = false)
    @JsonBackReference
    private Incident incident;
}
