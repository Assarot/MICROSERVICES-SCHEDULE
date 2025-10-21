package pe.edu.upeu.microservice_incident.infrastructure.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_incident.application.dto.IncidentDTO;
import pe.edu.upeu.microservice_incident.application.service.IncidentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/incidents")
@RequiredArgsConstructor
public class IncidentController {
    
    private final IncidentService incidentService;
    
    @PostMapping
    public ResponseEntity<IncidentDTO> create(@Valid @RequestBody IncidentDTO dto) {
        return new ResponseEntity<>(incidentService.create(dto), HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<IncidentDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.findById(id));
    }
    
    @GetMapping
    public ResponseEntity<List<IncidentDTO>> findAll() {
        return ResponseEntity.ok(incidentService.findAll());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<IncidentDTO> update(@PathVariable Long id, @Valid @RequestBody IncidentDTO dto) {
        return ResponseEntity.ok(incidentService.update(id, dto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        incidentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
