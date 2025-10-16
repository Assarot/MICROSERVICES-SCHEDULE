package pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_schedule.domain.port.in.ScheduleUseCase;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.dto.ScheduleRequest;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.dto.ScheduleResponse;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.mapper.ScheduleDtoMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    
    private final ScheduleUseCase useCase;
    private final ScheduleDtoMapper mapper;

    @PostMapping
    public ResponseEntity<ScheduleResponse> create(@Valid @RequestBody ScheduleRequest request) {
        var domain = mapper.toDomain(request);
        var created = useCase.create(domain);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponse> findById(@PathVariable Long id) {
        return useCase.findById(id)
                .map(mapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> findAll() {
        var result = useCase.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ScheduleRequest request) {
        var domain = mapper.toDomain(request);
        var updated = useCase.update(id, domain);
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        useCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
