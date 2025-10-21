package pe.edu.upeu.microservice_incident.application.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.microservice_incident.application.dto.SeverityDTO;
import pe.edu.upeu.microservice_incident.domain.model.Severity;
import pe.edu.upeu.microservice_incident.domain.port.SeverityRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeverityService {
    
    private final SeverityRepository severityRepository;
    private final ModelMapper modelMapper;
    
    @Transactional
    public SeverityDTO create(SeverityDTO dto) {
        if (severityRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Severity with name " + dto.getName() + " already exists");
        }
        Severity severity = modelMapper.map(dto, Severity.class);
        Severity saved = severityRepository.save(severity);
        return modelMapper.map(saved, SeverityDTO.class);
    }
    
    @Transactional(readOnly = true)
    public SeverityDTO findById(Long id) {
        Severity severity = severityRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Severity not found with id: " + id));
        return modelMapper.map(severity, SeverityDTO.class);
    }
    
    @Transactional(readOnly = true)
    public List<SeverityDTO> findAll() {
        return severityRepository.findAll().stream()
            .map(severity -> modelMapper.map(severity, SeverityDTO.class))
            .collect(Collectors.toList());
    }
    
    @Transactional
    public SeverityDTO update(Long id, SeverityDTO dto) {
        Severity severity = severityRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Severity not found with id: " + id));
        
        if (!severity.getName().equals(dto.getName()) && severityRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Severity with name " + dto.getName() + " already exists");
        }
        
        severity.setName(dto.getName());
        severity.setIsActive(dto.getIsActive());
        
        Severity updated = severityRepository.save(severity);
        return modelMapper.map(updated, SeverityDTO.class);
    }
    
    @Transactional
    public void delete(Long id) {
        if (!severityRepository.findById(id).isPresent()) {
            throw new RuntimeException("Severity not found with id: " + id);
        }
        severityRepository.deleteById(id);
    }
}
