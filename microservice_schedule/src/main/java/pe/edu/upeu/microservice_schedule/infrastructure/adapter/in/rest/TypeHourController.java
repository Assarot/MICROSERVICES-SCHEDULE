package pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_schedule.domain.port.in.TypeHourUseCase;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.dto.TypeHourRequest;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.dto.TypeHourResponse;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.mapper.TypeHourDtoMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/type-hours")
@RequiredArgsConstructor
public class TypeHourController {
    
    private final TypeHourUseCase useCase;
    private final TypeHourDtoMapper mapper;

    @PostMapping
    public ResponseEntity<TypeHourResponse> create(@Valid @RequestBody TypeHourRequest request) {
        var domain = mapper.toDomain(request);
        var created = useCase.create(domain);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeHourResponse> findById(@PathVariable Long id) {
        return useCase.findById(id)
                .map(mapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TypeHourResponse>> findAll() {
        var result = useCase.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TypeHourResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TypeHourRequest request) {
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
