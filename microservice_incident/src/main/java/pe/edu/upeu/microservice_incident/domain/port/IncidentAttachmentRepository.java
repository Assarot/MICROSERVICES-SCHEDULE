package pe.edu.upeu.microservice_incident.domain.port;

import pe.edu.upeu.microservice_incident.domain.model.IncidentAttachment;

import java.util.List;
import java.util.Optional;

public interface IncidentAttachmentRepository {
    IncidentAttachment save(IncidentAttachment attachment);
    Optional<IncidentAttachment> findById(Long id);
    List<IncidentAttachment> findAll();
    List<IncidentAttachment> findByIncidentId(Long incidentId);
    void deleteById(Long id);
}
