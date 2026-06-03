package pe.edu.upeu.microservice_reservation.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_reservation.infrastructure.client.dto.ReservationBlockClientDto;
import pe.edu.upeu.microservice_reservation.infrastructure.client.dto.ReservationBlockResponseDto;

/**
 * Feign client para comunicarse con MS-SCHEDULE.
 * Nombre Eureka: microservice-schedule (puerto 8084)
 * MS-SCHEDULE no tiene security, las llamadas se hacen directamente.
 */
@FeignClient(name = "microservice-schedule")
public interface ScheduleClient {

    /**
     * Bloquea un horario al aprobar una reserva.
     * POST /api/v1/schedules/reservation-blocks
     * Retorna 201 si bloqueado, 409 si hay conflicto.
     */
    @PostMapping("/api/v1/schedules/reservation-blocks")
    ResponseEntity<ReservationBlockResponseDto> createReservationBlock(
            @RequestBody ReservationBlockClientDto request);

    /**
     * Libera el bloqueo al revocar una reserva.
     * DELETE /api/v1/schedules/reservation-blocks/{id}
     */
    @DeleteMapping("/api/v1/schedules/reservation-blocks/{id}")
    ResponseEntity<Void> releaseReservationBlock(@PathVariable("id") Long id);
}
