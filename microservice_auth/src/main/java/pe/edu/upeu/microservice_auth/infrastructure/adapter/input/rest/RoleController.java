package pe.edu.upeu.microservice_auth.infrastructure.adapter.input.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_auth.domain.model.Role;
import pe.edu.upeu.microservice_auth.domain.port.output.RoleRepositoryPort;
import pe.edu.upeu.microservice_auth.infrastructure.adapter.input.dto.RoleDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/auth/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleRepositoryPort roleRepositoryPort;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleDTO>> getAll() {
        List<RoleDTO> roles = roleRepositoryPort.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDTO> getById(@PathVariable Long id) {
        Role role = roleRepositoryPort.findById(id).orElseThrow();
        return ResponseEntity.ok(toDto(role));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDTO> create(@Valid @RequestBody RoleDTO dto) {
        Role toSave = Role.builder()
                .name(dto.getName())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : Boolean.TRUE)
                .build();
        Role saved = roleRepositoryPort.save(toSave);
        return ResponseEntity.ok(toDto(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDTO> update(@PathVariable Long id, @Valid @RequestBody RoleDTO dto) {
        Role role = roleRepositoryPort.findById(id).orElseThrow();
        if (dto.getName() != null) role.setName(dto.getName());
        if (dto.getIsActive() != null) role.setIsActive(dto.getIsActive());
        Role saved = roleRepositoryPort.save(role);
        return ResponseEntity.ok(toDto(saved));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String,String>> delete(@PathVariable Long id) {
        roleRepositoryPort.deleteById(id);
        return ResponseEntity.ok(Map.of("message","Role deleted"));
    }

    private RoleDTO toDto(Role r) {
        return RoleDTO.builder()
                .id(r.getIdRole())
                .name(r.getName())
                .isActive(r.getIsActive())
                .build();
    }
}
