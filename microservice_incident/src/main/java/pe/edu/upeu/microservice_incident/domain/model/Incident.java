package pe.edu.upeu.microservice_incident.domain.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "incident")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Incident {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_incident")
    private Long idIncident;
    
    @Column(nullable = false, length = 255)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "report_date", nullable = false)
    private LocalDateTime reportDate;
    
    @Column(name = "resolution_date")
    private LocalDateTime resolutionDate;
    
    @Column(name = "id_reported_by")
    private Long idReportedBy;
    
    @Column(name = "id_resolved_by")
    private Long idResolvedBy;
    
    @Column(name = "id_academic_space")
    private Long idAcademicSpace;
    
    @Column(name = "id_resource")
    private Long idResource;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_severity", nullable = false)
    private Severity severity;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_status", nullable = false)
    private Status status;
    
    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<IncidentAttachment> attachments;
}
