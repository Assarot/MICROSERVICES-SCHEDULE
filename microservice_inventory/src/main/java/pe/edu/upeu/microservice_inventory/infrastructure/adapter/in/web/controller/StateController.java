package pe.edu.upeu.microservice_inventory.infrastructure.adapter.in.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_inventory.domain.port.in.StateUseCase;
import pe.edu.upeu.microservice_inventory.infrastructure.adapter.in.web.dto.StateDto;
import pe.edu.upeu.microservice_inventory.infrastructure.adapter.in.web.mapper.WebMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/states")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StateController {
    
    private final StateUseCase stateUseCase;
    private final WebMapper mapper;

    @PostMapping
    public ResponseEntity<StateDto> createState(@Valid @RequestBody StateDto stateDto) {
        var state = mapper.toStateDomain(stateDto);
        var createdState = stateUseCase.createState(state);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toStateDto(createdState));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StateDto> updateState(@PathVariable Long id, @Valid @RequestBody StateDto stateDto) {
        var state = mapper.toStateDomain(stateDto);
        var updatedState = stateUseCase.updateState(id, state);
        return ResponseEntity.ok(mapper.toStateDto(updatedState));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteState(@PathVariable Long id) {
        stateUseCase.deleteState(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StateDto> getStateById(@PathVariable Long id) {
        return stateUseCase.getStateById(id)
                .map(state -> ResponseEntity.ok(mapper.toStateDto(state)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<StateDto>> getAllStates() {
        var states = stateUseCase.getAllStates();
        var stateDtos = states.stream()
                .map(mapper::toStateDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(stateDtos);
    }

    @GetMapping("/active")
    public ResponseEntity<List<StateDto>> getActiveStates() {
        var states = stateUseCase.getActiveStates();
        var stateDtos = states.stream()
                .map(mapper::toStateDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(stateDtos);
    }
}
