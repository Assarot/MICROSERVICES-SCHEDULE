package pe.edu.upeu.microservice_reservation.infrastructure.adapter.in.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_reservation.application.dto.request.BatchApproveRequest;
import pe.edu.upeu.microservice_reservation.application.dto.request.ReservationCreateRequest;
import pe.edu.upeu.microservice_reservation.application.dto.request.StatusChangeRequest;
import pe.edu.upeu.microservice_reservation.application.dto.response.BatchApproveResult;
import pe.edu.upeu.microservice_reservation.application.dto.response.ReservationLogResponse;
import pe.edu.upeu.microservice_reservation.application.dto.response.ReservationResponse;
import pe.edu.upeu.microservice_reservation.application.service.ReservationService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller principal de reservas.
 * Todos los endpoints requieren autenticación JWT.
 * Los que requieren ADMIN usan @PreAuthorize.
 */
@Slf4j
@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // ======================================================================
    // CRUD BÁSICO
    // ======================================================================

    /**
     * Crear reserva — Casos 9, 10, 11, 14, 15
     * POST /api/reservations
     * USER y ADMIN pueden registrar solicitudes de reserva.
     */
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody ReservationCreateRequest request,
            HttpServletRequest httpRequest) {

        Long authenticatedUserId = getAuthenticatedUserId(httpRequest);
        log.info("POST /api/reservations - user={}", authenticatedUserId);
        ReservationResponse response = reservationService.createReservation(request, authenticatedUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Obtener todas las reservas
     * GET /api/reservations
     * Solo ADMIN puede ver todas las reservas.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('COOROOMS')")
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    /**
     * Obtener reserva por ID
     * GET /api/reservations/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    /**
     * Obtener reservas propias — Caso 16
     * GET /api/reservations/student/{idUserProfile}
     * Solo USER puede ver sus propias reservas.
     * ADMIN puede ver las de cualquier usuario.
     */
    @GetMapping("/student/{idUserProfile}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<ReservationResponse>> getStudentReservations(
            @PathVariable Long idUserProfile,
            HttpServletRequest httpRequest) {

        Long authenticatedUserId = getAuthenticatedUserId(httpRequest);
        boolean isAdmin = isAdmin(httpRequest);

        // USER solo puede ver sus propias reservas, no las de otro usuario
        if (!isAdmin && !idUserProfile.equals(authenticatedUserId)) {
            throw new org.springframework.security.access.AccessDeniedException(
                "No tiene permiso para ver las reservas de otro usuario.");
        }

        return ResponseEntity.ok(reservationService.getReservationsByStudent(idUserProfile));
    }

    /**
     * Historial de reservas propias con filtros — Caso 16
     * GET /api/reservations/student/{idUserProfile}/history
     * Solo USER puede consultar su propio historial con sus estados.
     * ADMIN puede consultar el historial de cualquier usuario.
     */
    @GetMapping("/student/{idUserProfile}/history")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<ReservationResponse>> getStudentHistory(
            @PathVariable Long idUserProfile,
            @RequestParam(required = false) Long idStatus,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endTo,
            @RequestParam(required = false) Long idAcademicSpace,
            @RequestParam(required = false) Long idCourse,
            HttpServletRequest httpRequest) {

        Long authenticatedUserId = getAuthenticatedUserId(httpRequest);
        boolean isAdmin = isAdmin(httpRequest);

        // USER solo puede ver su propio historial
        if (!isAdmin && !idUserProfile.equals(authenticatedUserId)) {
            throw new org.springframework.security.access.AccessDeniedException(
                "No tiene permiso para ver el historial de otro usuario.");
        }

        return ResponseEntity.ok(reservationService.getStudentHistory(
                idUserProfile, idStatus, startFrom, endTo, idAcademicSpace, idCourse));
    }

    // ======================================================================
    // CAMBIOS DE ESTADO
    // ======================================================================

    /**
     * Cancelar (anular) propia solicitud de reserva — Caso 12, 13
     * PUT /api/reservations/{id}/cancel
     * Solo USER puede cancelar su propia solicitud (estado Pendiente).
     * El servicio valida que sea el dueño de la reserva.
     */
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReservationResponse> cancelReservation(
            @PathVariable Long id,
            @RequestBody(required = false) StatusChangeRequest request,
            HttpServletRequest httpRequest) {

        Long authenticatedUserId = getAuthenticatedUserId(httpRequest);
        boolean isAdmin = isAdmin(httpRequest);
        return ResponseEntity.ok(reservationService.cancelReservation(id, request, authenticatedUserId, isAdmin));
    }

    /**
     * Aprobar reserva — Caso 17, 19, 23
     * PUT /api/reservations/{id}/approve
     * Solo ADMIN
     */
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COOROOMS')")
    public ResponseEntity<ReservationResponse> approveReservation(
            @PathVariable Long id,
            @Valid @RequestBody StatusChangeRequest request,
            HttpServletRequest httpRequest) {

        Long authenticatedUserId = getAuthenticatedUserId(httpRequest);
        log.info("PUT /api/reservations/{}/approve - admin={}", id, authenticatedUserId);
        return ResponseEntity.ok(reservationService.approveReservation(id, request, authenticatedUserId));
    }

    /**
     * Rechazar reserva — Caso 18
     * PUT /api/reservations/{id}/reject
     * Solo ADMIN
     */
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COOROOMS')")
    public ResponseEntity<ReservationResponse> rejectReservation(
            @PathVariable Long id,
            @Valid @RequestBody StatusChangeRequest request,
            HttpServletRequest httpRequest) {

        Long authenticatedUserId = getAuthenticatedUserId(httpRequest);
        return ResponseEntity.ok(reservationService.rejectReservation(id, request, authenticatedUserId));
    }

    /**
     * Finalizar reserva
     * PUT /api/reservations/{id}/finish
     * Solo ADMIN
     */
    @PutMapping("/{id}/finish")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COOROOMS')")
    public ResponseEntity<ReservationResponse> finishReservation(
            @PathVariable Long id,
            @RequestBody(required = false) StatusChangeRequest request,
            HttpServletRequest httpRequest) {

        Long authenticatedUserId = getAuthenticatedUserId(httpRequest);
        return ResponseEntity.ok(reservationService.finishReservation(id, request, authenticatedUserId));
    }

    /**
     * Revocar reserva aprobada — Caso 22
     * PUT /api/reservations/{id}/revoke
     * Solo ADMIN
     */
    @PutMapping("/{id}/revoke")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COOROOMS')")
    public ResponseEntity<ReservationResponse> revokeReservation(
            @PathVariable Long id,
            @Valid @RequestBody StatusChangeRequest request,
            HttpServletRequest httpRequest) {

        Long authenticatedUserId = getAuthenticatedUserId(httpRequest);
        return ResponseEntity.ok(reservationService.revokeReservation(id, request, authenticatedUserId));
    }

    /**
     * Deshacer decisión (volver a pendiente)
     * PUT /api/reservations/{id}/revert-to-pending
     * Solo ADMIN
     */
    @PutMapping("/{id}/revert-to-pending")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COOROOMS')")
    public ResponseEntity<ReservationResponse> revertToPending(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {

        Long authenticatedUserId = getAuthenticatedUserId(httpRequest);
        return ResponseEntity.ok(reservationService.revertToPending(id, authenticatedUserId));
    }

    /**
     * Aprobación en lote — Caso 20, 21
     * POST /api/reservations/batch-approve
     * Solo ADMIN
     */
    @PostMapping("/batch-approve")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COOROOMS')")
    public ResponseEntity<List<BatchApproveResult>> batchApprove(
            @Valid @RequestBody BatchApproveRequest request,
            HttpServletRequest httpRequest) {

        Long authenticatedUserId = getAuthenticatedUserId(httpRequest);
        log.info("POST /api/reservations/batch-approve - admin={}, count={}", 
                authenticatedUserId, request.getReservationIds().size());
        return ResponseEntity.ok(reservationService.batchApprove(request, authenticatedUserId));
    }

    /**
     * Historial de logs de una reserva
     * GET /api/reservations/{id}/logs
     */
    @GetMapping("/{id}/logs")
    public ResponseEntity<List<ReservationLogResponse>> getReservationLogs(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationLogs(id));
    }

    // ======================================================================
    // HELPERS PRIVADOS — Extraen info del JWT del request
    // ======================================================================

    private Long getAuthenticatedUserId(HttpServletRequest request) {
        Object userId = request.getAttribute("authenticatedUserId");
        if (userId instanceof Long) {
            return (Long) userId;
        }
        if (userId instanceof Number) {
            return ((Number) userId).longValue();
        }
        throw new RuntimeException("No se pudo determinar el ID del usuario autenticado del token JWT.");
    }

    @SuppressWarnings("unchecked")
    private boolean isAdmin(HttpServletRequest request) {
        Object roles = request.getAttribute("authenticatedRoles");
        if (roles instanceof List<?>) {
            return ((List<String>) roles).contains("ADMIN");
        }
        return false;
    }
}
