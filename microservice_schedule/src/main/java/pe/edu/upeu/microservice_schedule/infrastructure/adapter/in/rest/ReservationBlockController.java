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
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.repository.ScheduleJpaRepository;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

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
    private final ScheduleJpaRepository scheduleRepository;

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
    public ResponseEntity<Void> releaseBlock(@PathVariable("id") Long id) {
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
            @RequestParam("idAcademicSpace") Long idAcademicSpace,
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam("startTime") @DateTimeFormat(pattern = "HH:mm:ss") LocalTime startTime,
            @RequestParam("endTime") @DateTimeFormat(pattern = "HH:mm:ss") LocalTime endTime) {

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

    /**
     * Retorna una lista de IDs de espacios académicos ocupados para una fecha y hora dadas.
     * Revisa tanto bloqueos de reservas como horarios regulares.
     * GET /api/v1/schedules/reservation-blocks/occupied-spaces
     */
    @GetMapping("/occupied-spaces")
    public ResponseEntity<List<Long>> getOccupiedSpaces(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam("startTime") @DateTimeFormat(pattern = "HH:mm:ss") LocalTime startTime,
            @RequestParam("endTime") @DateTimeFormat(pattern = "HH:mm:ss") LocalTime endTime) {

        log.info("GET /api/v1/schedules/reservation-blocks/occupied-spaces - date={}, start={}, end={}",
                date, startTime, endTime);

        Set<Long> occupiedSpaces = new HashSet<>();

        // 1. Obtener los espacios ocupados por reservas en esta fecha y horario
        List<Long> blockedByReservations = reservationBlockRepository.findOccupiedSpaceIds(date, startTime, endTime);
        if (blockedByReservations != null) {
            occupiedSpaces.addAll(blockedByReservations);
        }

        // 2. Obtener los espacios ocupados por horarios regulares de cursos
        // date.getDayOfWeek().getValue() devuelve 1 para Lunes, 7 para Domingo (ISO-8601).
        // En la BD (tabla week_day): 1=Domingo, 2=Lunes, 3=Martes... 7=Sábado.
        int isoDay = date.getDayOfWeek().getValue();
        Long idWeekName = (long) ((isoDay % 7) + 1);
        List<Long> blockedBySchedules = scheduleRepository.findOccupiedSpaceIds(idWeekName, startTime, endTime);
        if (blockedBySchedules != null) {
            occupiedSpaces.addAll(blockedBySchedules);
        }

        return ResponseEntity.ok(occupiedSpaces.stream().collect(Collectors.toList()));
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
