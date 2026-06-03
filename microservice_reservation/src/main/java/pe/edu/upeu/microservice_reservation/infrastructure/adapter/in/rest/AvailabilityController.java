package pe.edu.upeu.microservice_reservation.infrastructure.adapter.in.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_reservation.application.dto.response.AvailabilityResponse;
import pe.edu.upeu.microservice_reservation.application.service.AvailabilityService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Controller para consulta de disponibilidad de espacios académicos.
 * Todos los roles autenticados pueden consultar disponibilidad.
 * Casos: 1, 2, 3, 4, 5, 6, 7, 8
 */
@Slf4j
@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    /**
     * Consulta disponibilidad de espacios académicos.
     * Caso 1: Disponibilidad futura con resultados
     * Caso 2: Ambiente ocupado no aparece
     * Caso 3: Filtro minCapacity
     * Caso 4: Todos ocupados → lista vacía con mensaje
     * Caso 5: Solapamiento parcial detectado correctamente
     * Caso 6: Sin autenticación → 401 (manejado por SecurityConfig)
     * Caso 7: Filtro por tipo de ambiente
     * Caso 8: Fecha > 1 año → 400
     *
     * GET /api/reservations/availability
     */
    @GetMapping("/availability")
    public ResponseEntity<Map<String, Object>> checkAvailability(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startDatetime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endDatetime,
            @RequestParam(required = false) Integer minCapacity,
            @RequestParam(required = false) String typeAcademicSpace,
            @RequestParam(required = false) Long idAcademicSpace,
            Authentication authentication) {

        log.info("GET /api/reservations/availability - user={}, start={}, end={}, minCap={}, type={}, idSpace={}",
                authentication.getName(), startDatetime, endDatetime, minCapacity, typeAcademicSpace, idAcademicSpace);

        List<AvailabilityResponse> available = availabilityService.checkAvailability(
                startDatetime, endDatetime, minCapacity, typeAcademicSpace, idAcademicSpace);

        String message = available.isEmpty()
                ? "No hay espacios académicos disponibles para el horario solicitado"
                : available.size() + " espacio(s) disponible(s) encontrado(s)";

        return ResponseEntity.ok(Map.of(
                "startDatetime", startDatetime.toString(),
                "endDatetime", endDatetime.toString(),
                "availableSpaces", available,
                "totalAvailable", available.size(),
                "message", message
        ));
    }
}
