package pe.edu.upeu.microservice_incident.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.microservice_incident.application.dto.IncidentDTO;
import pe.edu.upeu.microservice_incident.domain.model.Incident;
import pe.edu.upeu.microservice_incident.domain.model.Severity;
import pe.edu.upeu.microservice_incident.domain.model.Status;
import pe.edu.upeu.microservice_incident.domain.port.IncidentRepository;
import pe.edu.upeu.microservice_incident.domain.port.SeverityRepository;
import pe.edu.upeu.microservice_incident.domain.port.StatusRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncidentService {
    
    private final IncidentRepository incidentRepository;
    private final SeverityRepository severityRepository;
    private final StatusRepository statusRepository;
    
    @Transactional
    public IncidentDTO create(IncidentDTO dto) {
        Severity severity = severityRepository.findById(dto.getIdSeverity())
            .orElseThrow(() -> new RuntimeException("Severity not found with id: " + dto.getIdSeverity()));
        
        Status status = statusRepository.findById(dto.getIdStatus())
            .orElseThrow(() -> new RuntimeException("Status not found with id: " + dto.getIdStatus()));
        
        Incident incident = new Incident();
        incident.setTitle(dto.getTitle());
        incident.setDescription(dto.getDescription());
        incident.setReportDate(dto.getReportDate());
        incident.setResolutionDate(dto.getResolutionDate());
        incident.setIdReportedBy(dto.getIdReportedBy());
        incident.setIdResolvedBy(dto.getIdResolvedBy());
        incident.setIdAcademicSpace(dto.getIdAcademicSpace());
        incident.setIdResource(dto.getIdResource());
        incident.setSeverity(severity);
        incident.setStatus(status);
        
        Incident saved = incidentRepository.save(incident);
        return convertToDTO(saved);
    }
    
    @Transactional(readOnly = true)
    public IncidentDTO findById(Long id) {
        Incident incident = incidentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Incident not found with id: " + id));
        return convertToDTO(incident);
    }
    
    @Transactional(readOnly = true)
    public List<IncidentDTO> findAll() {
        return incidentRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public IncidentDTO update(Long id, IncidentDTO dto) {
        Incident incident = incidentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Incident not found with id: " + id));
        
        Severity severity = severityRepository.findById(dto.getIdSeverity())
            .orElseThrow(() -> new RuntimeException("Severity not found with id: " + dto.getIdSeverity()));
        
        Status status = statusRepository.findById(dto.getIdStatus())
            .orElseThrow(() -> new RuntimeException("Status not found with id: " + dto.getIdStatus()));
        
        incident.setTitle(dto.getTitle());
        incident.setDescription(dto.getDescription());
        incident.setReportDate(dto.getReportDate());
        incident.setResolutionDate(dto.getResolutionDate());
        incident.setIdReportedBy(dto.getIdReportedBy());
        incident.setIdResolvedBy(dto.getIdResolvedBy());
        incident.setIdAcademicSpace(dto.getIdAcademicSpace());
        incident.setIdResource(dto.getIdResource());
        incident.setSeverity(severity);
        incident.setStatus(status);
        
        Incident updated = incidentRepository.save(incident);
        return convertToDTO(updated);
    }
    
    @Transactional
    public void delete(Long id) {
        if (!incidentRepository.findById(id).isPresent()) {
            throw new RuntimeException("Incident not found with id: " + id);
        }
        incidentRepository.deleteById(id);
    }
    
    private IncidentDTO convertToDTO(Incident incident) {
        IncidentDTO dto = new IncidentDTO();
        dto.setIdIncident(incident.getIdIncident());
        dto.setTitle(incident.getTitle());
        dto.setDescription(incident.getDescription());
        dto.setReportDate(incident.getReportDate());
        dto.setResolutionDate(incident.getResolutionDate());
        dto.setIdReportedBy(incident.getIdReportedBy());
        dto.setIdResolvedBy(incident.getIdResolvedBy());
        dto.setIdAcademicSpace(incident.getIdAcademicSpace());
        dto.setIdResource(incident.getIdResource());
        dto.setIdSeverity(incident.getSeverity().getIdSeverity());
        dto.setIdStatus(incident.getStatus().getIdStatus());
        return dto;
    }
}
