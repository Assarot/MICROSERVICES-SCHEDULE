package pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.dto.ReservationBlockRequest;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.dto.ReservationBlockResponse;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.entity.ReservationBlockEntity;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.repository.ReservationBlockJpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

/**
 * Controller para gestionar bloqueos de horario generados por reservas.
 * Diferente a los horarios académicos regulares.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/schedules/reservation-blocks")
@RequiredArgsConstructor
public class ReservationBlockController {

    private final ReservationBlockJpaRepository reservationBlockRepository;

    /**
     * Bloquea un horario en un espacio académico a partir de una reserva aprobada.
     * POST /api/v1/schedules/reservation-blocks
     */
    @PostMapping
    public ResponseEntity<ReservationBlockResponse> createBlock(
            @Valid @RequestBody ReservationBlockRequest request) {

        log.info("POST /api/v1/schedules/reservation-blocks - Bloqueando horario para idAcademicSpace={}, date={}, start={}, end={}",
                request.getIdAcademicSpace(), request.getDate(), request.getStartTime(), request.getEndTime());

        // Verificar si ya existe un bloqueo que se solape
        boolean overlaps = reservationBlockRepository.existsOverlappingBlock(
                request.getIdAcademicSpace(),
                request.getDate(),
                request.getStartTime(),
                request.getEndTime()
        );

        if (overlaps) {
            log.warn("Conflicto de horario detectado para idAcademicSpace={} en fecha={}", 
                    request.getIdAcademicSpace(), request.getDate());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .build();
        }

        ReservationBlockEntity entity = ReservationBlockEntity.builder()
                .idAcademicSpace(request.getIdAcademicSpace())
                .blockDate(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .idReservation(request.getIdReservation())
                .isActive(true)
                .build();

        ReservationBlockEntity saved = reservationBlockRepository.save(entity);

        log.info("Bloqueo creado con id={}", saved.getIdReservationBlock());

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    /**
     * Libera el bloqueo de un horario (al revocar una reserva).
     * DELETE /api/v1/schedules/reservation-blocks/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> releaseBlock(@PathVariable Long id) {
        log.info("DELETE /api/v1/schedules/reservation-blocks/{} - Liberando bloqueo", id);

        return reservationBlockRepository.findById(id).map(block -> {
            block.setIsActive(false);
            reservationBlockRepository.save(block);
            log.info("Bloqueo {} liberado", id);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().<Void>build());
    }

    /**
     * Verifica disponibilidad de un espacio académico para un rango de fecha/hora.
     * GET /api/v1/schedules/availability
     */
    @GetMapping("/availability")
    public ResponseEntity<Map<String, Object>> checkAvailability(
            @RequestParam Long idAcademicSpace,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam @DateTimeFormat(pattern = "HH:mm:ss") LocalTime startTime,
            @RequestParam @DateTimeFormat(pattern = "HH:mm:ss") LocalTime endTime) {

        log.info("GET /api/v1/schedules/availability - idAcademicSpace={}, date={}, start={}, end={}",
                idAcademicSpace, date, startTime, endTime);

        boolean blocked = reservationBlockRepository.existsOverlappingBlock(
                idAcademicSpace, date, startTime, endTime);

        return ResponseEntity.ok(Map.of(
                "idAcademicSpace", idAcademicSpace,
                "date", date.toString(),
                "startTime", startTime.toString(),
                "endTime", endTime.toString(),
                "available", !blocked
        ));
    }

    private ReservationBlockResponse toResponse(ReservationBlockEntity entity) {
        return ReservationBlockResponse.builder()
                .idReservationBlock(entity.getIdReservationBlock())
                .idAcademicSpace(entity.getIdAcademicSpace())
                .blockDate(entity.getBlockDate())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .idReservation(entity.getIdReservation())
                .isActive(entity.getIsActive())
                .blocked(true)
                .build();
    }
}
