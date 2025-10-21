package pe.edu.upeu.microservice_incident.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.microservice_incident.domain.model.IncidentAttachment;

import java.util.List;

public interface IncidentAttachmentJpaRepository extends JpaRepository<IncidentAttachment, Long> {
    List<IncidentAttachment> findByIncident_IdIncident(Long incidentId);
}
