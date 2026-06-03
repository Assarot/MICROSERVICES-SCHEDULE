package pe.edu.upeu.microservice_reservation.infrastructure.adapter.in.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_reservation.application.dto.request.AddMemberRequest;
import pe.edu.upeu.microservice_reservation.application.dto.response.ReservationMemberResponse;
import pe.edu.upeu.microservice_reservation.application.service.ReservationMemberService;

import java.util.List;

/**
 * Controller para gestión de integrantes de una reserva.
 */
@Slf4j
@RestController
@RequestMapping("/api/reservations/{idReservation}/members")
@RequiredArgsConstructor
public class ReservationMemberController {

    private final ReservationMemberService memberService;

    /**
     * Agregar un integrante a la reserva.
     * POST /api/reservations/{idReservation}/members
     */
    @PostMapping
    public ResponseEntity<ReservationMemberResponse> addMember(
            @PathVariable Long idReservation,
            @Valid @RequestBody AddMemberRequest request,
            HttpServletRequest httpRequest) {

        Long authenticatedUserId = getAuthenticatedUserId(httpRequest);
        log.info("POST /api/reservations/{}/members - user={}", idReservation, authenticatedUserId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(memberService.addMember(idReservation, request, authenticatedUserId));
    }

    /**
     * Listar integrantes de una reserva.
     * GET /api/reservations/{idReservation}/members
     */
    @GetMapping
    public ResponseEntity<List<ReservationMemberResponse>> getMembersByReservation(
            @PathVariable Long idReservation) {
        return ResponseEntity.ok(memberService.getMembersByReservation(idReservation));
    }

    /**
     * Eliminar un integrante de la reserva.
     * DELETE /api/reservations/{idReservation}/members/{idMember}
     */
    @DeleteMapping("/{idMember}")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long idReservation,
            @PathVariable Long idMember,
            HttpServletRequest httpRequest) {

        Long authenticatedUserId = getAuthenticatedUserId(httpRequest);
        boolean isAdmin = isAdmin(httpRequest);
        log.info("DELETE /api/reservations/{}/members/{} - user={}", idReservation, idMember, authenticatedUserId);
        memberService.removeMember(idReservation, idMember);
        return ResponseEntity.noContent().build();
    }

    private Long getAuthenticatedUserId(HttpServletRequest request) {
        Object userId = request.getAttribute("authenticatedUserId");
        if (userId instanceof Long) return (Long) userId;
        if (userId instanceof Number) return ((Number) userId).longValue();
        throw new RuntimeException("No se pudo determinar el ID del usuario autenticado.");
    }

    @SuppressWarnings("unchecked")
    private boolean isAdmin(HttpServletRequest request) {
        Object roles = request.getAttribute("authenticatedRoles");
        if (roles instanceof List<?>) return ((List<String>) roles).contains("ADMIN");
        return false;
    }
}
