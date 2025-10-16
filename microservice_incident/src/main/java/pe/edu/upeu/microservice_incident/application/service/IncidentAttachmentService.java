package pe.edu.upeu.microservice_incident.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.microservice_incident.application.dto.IncidentAttachmentDTO;
import pe.edu.upeu.microservice_incident.domain.model.Incident;
import pe.edu.upeu.microservice_incident.domain.model.IncidentAttachment;
import pe.edu.upeu.microservice_incident.domain.port.IncidentAttachmentRepository;
import pe.edu.upeu.microservice_incident.domain.port.IncidentRepository;
import pe.edu.upeu.microservice_incident.infrastructure.service.CloudinaryService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncidentAttachmentService {
    
    private final IncidentAttachmentRepository attachmentRepository;
    private final IncidentRepository incidentRepository;
    private final CloudinaryService cloudinaryService;
    
    @Transactional
    public IncidentAttachmentDTO create(Long incidentId, MultipartFile file) throws IOException {
        Incident incident = incidentRepository.findById(incidentId)
            .orElseThrow(() -> new RuntimeException("Incident not found with id: " + incidentId));
        
        String photoUrl = cloudinaryService.uploadImage(file);
        
        IncidentAttachment attachment = new IncidentAttachment();
        attachment.setPhotoUrl(photoUrl);
        attachment.setUploadedAt(LocalDateTime.now());
        attachment.setIncident(incident);
        
        IncidentAttachment saved = attachmentRepository.save(attachment);
        return convertToDTO(saved);
    }
    
    @Transactional(readOnly = true)
    public IncidentAttachmentDTO findById(Long id) {
        IncidentAttachment attachment = attachmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Attachment not found with id: " + id));
        return convertToDTO(attachment);
    }
    
    @Transactional(readOnly = true)
    public List<IncidentAttachmentDTO> findAll() {
        return attachmentRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<IncidentAttachmentDTO> findByIncidentId(Long incidentId) {
        return attachmentRepository.findByIncidentId(incidentId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public void delete(Long id) {
        if (!attachmentRepository.findById(id).isPresent()) {
            throw new RuntimeException("Attachment not found with id: " + id);
        }
        attachmentRepository.deleteById(id);
    }
    
    private IncidentAttachmentDTO convertToDTO(IncidentAttachment attachment) {
        IncidentAttachmentDTO dto = new IncidentAttachmentDTO();
        dto.setIdIncidentAttachment(attachment.getIdIncidentAttachment());
        dto.setPhotoUrl(attachment.getPhotoUrl());
        dto.setUploadedAt(attachment.getUploadedAt());
        dto.setIdIncident(attachment.getIncident().getIdIncident());
        return dto;
    }
}
