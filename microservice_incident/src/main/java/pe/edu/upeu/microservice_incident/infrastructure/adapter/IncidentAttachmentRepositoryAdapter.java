package pe.edu.upeu.microservice_incident.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_incident.domain.model.IncidentAttachment;
import pe.edu.upeu.microservice_incident.domain.port.IncidentAttachmentRepository;
import pe.edu.upeu.microservice_incident.infrastructure.repository.jpa.IncidentAttachmentJpaRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class IncidentAttachmentRepositoryAdapter implements IncidentAttachmentRepository {
    
    private final IncidentAttachmentJpaRepository jpaRepository;
    
    @Override
    public IncidentAttachment save(IncidentAttachment attachment) {
        return jpaRepository.save(attachment);
    }
    
    @Override
    public Optional<IncidentAttachment> findById(Long id) {
        return jpaRepository.findById(id);
    }
    
    @Override
    public List<IncidentAttachment> findAll() {
        return jpaRepository.findAll();
    }
    
    @Override
    public List<IncidentAttachment> findByIncidentId(Long incidentId) {
        return jpaRepository.findByIncident_IdIncident(incidentId);
    }
    
    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
