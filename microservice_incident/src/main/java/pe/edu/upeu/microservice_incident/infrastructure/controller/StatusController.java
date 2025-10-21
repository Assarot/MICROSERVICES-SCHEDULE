package pe.edu.upeu.microservice_incident.infrastructure.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_incident.application.dto.StatusDTO;
import pe.edu.upeu.microservice_incident.application.service.StatusService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/statuses")
@RequiredArgsConstructor
public class StatusController {
    
    private final StatusService statusService;
    
    @PostMapping
    public ResponseEntity<StatusDTO> create(@Valid @RequestBody StatusDTO dto) {
        return new ResponseEntity<>(statusService.create(dto), HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<StatusDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(statusService.findById(id));
    }
    
    @GetMapping
    public ResponseEntity<List<StatusDTO>> findAll() {
        return ResponseEntity.ok(statusService.findAll());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<StatusDTO> update(@PathVariable Long id, @Valid @RequestBody StatusDTO dto) {
        return ResponseEntity.ok(statusService.update(id, dto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        statusService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
