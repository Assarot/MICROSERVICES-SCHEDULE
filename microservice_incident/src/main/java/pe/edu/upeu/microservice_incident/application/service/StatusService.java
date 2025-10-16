package pe.edu.upeu.microservice_incident.application.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.microservice_incident.application.dto.StatusDTO;
import pe.edu.upeu.microservice_incident.domain.model.Status;
import pe.edu.upeu.microservice_incident.domain.port.StatusRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatusService {
    
    private final StatusRepository statusRepository;
    private final ModelMapper modelMapper;
    
    @Transactional
    public StatusDTO create(StatusDTO dto) {
        if (statusRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Status with name " + dto.getName() + " already exists");
        }
        Status status = modelMapper.map(dto, Status.class);
        Status saved = statusRepository.save(status);
        return modelMapper.map(saved, StatusDTO.class);
    }
    
    @Transactional(readOnly = true)
    public StatusDTO findById(Long id) {
        Status status = statusRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Status not found with id: " + id));
        return modelMapper.map(status, StatusDTO.class);
    }
    
    @Transactional(readOnly = true)
    public List<StatusDTO> findAll() {
        return statusRepository.findAll().stream()
            .map(status -> modelMapper.map(status, StatusDTO.class))
            .collect(Collectors.toList());
    }
    
    @Transactional
    public StatusDTO update(Long id, StatusDTO dto) {
        Status status = statusRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Status not found with id: " + id));
        
        if (!status.getName().equals(dto.getName()) && statusRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Status with name " + dto.getName() + " already exists");
        }
        
        status.setName(dto.getName());
        status.setIsActive(dto.getIsActive());
        
        Status updated = statusRepository.save(status);
        return modelMapper.map(updated, StatusDTO.class);
    }
    
    @Transactional
    public void delete(Long id) {
        if (!statusRepository.findById(id).isPresent()) {
            throw new RuntimeException("Status not found with id: " + id);
        }
        statusRepository.deleteById(id);
    }
}
