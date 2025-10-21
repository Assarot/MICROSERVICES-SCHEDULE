package pe.edu.upeu.microservice_incident.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.microservice_incident.application.dto.IncidentAttachmentDTO;
import pe.edu.upeu.microservice_incident.application.service.IncidentAttachmentService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/attachments")
@RequiredArgsConstructor
public class IncidentAttachmentController {
    
    private final IncidentAttachmentService attachmentService;
    
    @PostMapping
    public ResponseEntity<IncidentAttachmentDTO> create(
            @RequestParam("incidentId") Long incidentId,
            @RequestParam("file") MultipartFile file) throws IOException {
        return new ResponseEntity<>(attachmentService.create(incidentId, file), HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<IncidentAttachmentDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(attachmentService.findById(id));
    }
    
    @GetMapping
    public ResponseEntity<List<IncidentAttachmentDTO>> findAll() {
        return ResponseEntity.ok(attachmentService.findAll());
    }
    
    @GetMapping("/incident/{incidentId}")
    public ResponseEntity<List<IncidentAttachmentDTO>> findByIncidentId(@PathVariable Long incidentId) {
        return ResponseEntity.ok(attachmentService.findByIncidentId(incidentId));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        attachmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
