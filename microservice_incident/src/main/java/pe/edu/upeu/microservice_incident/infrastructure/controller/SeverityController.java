package pe.edu.upeu.microservice_incident.infrastructure.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_incident.application.dto.SeverityDTO;
import pe.edu.upeu.microservice_incident.application.service.SeverityService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/severities")
@RequiredArgsConstructor
public class SeverityController {
    
    private final SeverityService severityService;
    
    @PostMapping
    public ResponseEntity<SeverityDTO> create(@Valid @RequestBody SeverityDTO dto) {
        return new ResponseEntity<>(severityService.create(dto), HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SeverityDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(severityService.findById(id));
    }
    
    @GetMapping
    public ResponseEntity<List<SeverityDTO>> findAll() {
        return ResponseEntity.ok(severityService.findAll());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<SeverityDTO> update(@PathVariable Long id, @Valid @RequestBody SeverityDTO dto) {
        return ResponseEntity.ok(severityService.update(id, dto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        severityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
