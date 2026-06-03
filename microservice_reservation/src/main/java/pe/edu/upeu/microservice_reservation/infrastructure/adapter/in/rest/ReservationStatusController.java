package pe.edu.upeu.microservice_reservation.infrastructure.adapter.in.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_reservation.application.dto.response.ReservationStatusResponse;
import pe.edu.upeu.microservice_reservation.infrastructure.adapter.out.persistence.repository.ReservationStatusJpaRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller para consultar los estados de reserva disponibles.
 * Los estados se cargan desde data.sql al iniciar la app.
 */
@Slf4j
@RestController
@RequestMapping("/api/reservation-status")
@RequiredArgsConstructor
public class ReservationStatusController {

    private final ReservationStatusJpaRepository statusRepository;

    /**
     * Listar todos los estados de reserva.
     * GET /api/reservation-status
     */
    @GetMapping
    public ResponseEntity<List<ReservationStatusResponse>> getAllStatuses() {
        List<ReservationStatusResponse> statuses = statusRepository.findAll()
                .stream()
                .map(s -> ReservationStatusResponse.builder()
                        .idStatus(s.getIdStatus())
                        .name(s.getName())
                        .isActive(s.getIsActive())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(statuses);
    }

    /**
     * Obtener un estado por ID.
     * GET /api/reservation-status/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReservationStatusResponse> getStatusById(@PathVariable Long id) {
        return statusRepository.findById(id)
                .map(s -> ReservationStatusResponse.builder()
                        .idStatus(s.getIdStatus())
                        .name(s.getName())
                        .isActive(s.getIsActive())
                        .build())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
