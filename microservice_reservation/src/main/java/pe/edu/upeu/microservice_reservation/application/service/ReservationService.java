package pe.edu.upeu.microservice_reservation.application.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.microservice_reservation.application.dto.request.BatchApproveRequest;
import pe.edu.upeu.microservice_reservation.application.dto.request.ReservationCreateRequest;
import pe.edu.upeu.microservice_reservation.application.dto.request.StatusChangeRequest;
import pe.edu.upeu.microservice_reservation.application.dto.response.*;
import pe.edu.upeu.microservice_reservation.infrastructure.adapter.out.persistence.entity.*;
import pe.edu.upeu.microservice_reservation.infrastructure.adapter.out.persistence.repository.*;
import pe.edu.upeu.microservice_reservation.infrastructure.client.AcademicSpaceClient;
import pe.edu.upeu.microservice_reservation.infrastructure.client.CourseClient;
import pe.edu.upeu.microservice_reservation.infrastructure.client.ScheduleClient;
import pe.edu.upeu.microservice_reservation.infrastructure.client.UserProfileClient;
import pe.edu.upeu.microservice_reservation.infrastructure.client.dto.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio principal de reservas.
 * Implementa todos los 23 casos funcionales obligatorios.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    // Repositorios internos
    private final ReservationJpaRepository reservationRepository;
    private final ReservationStatusJpaRepository statusRepository;
    private final ReservationMemberJpaRepository memberRepository;
    private final ReservationLogJpaRepository logRepository;

    // Feign Clients
    private final UserProfileClient userProfileClient;
    private final AcademicSpaceClient academicSpaceClient;
    private final ScheduleClient scheduleClient;
    private final CourseClient courseClient;

    // Servicios auxiliares
    private final HolidayPolicyService holidayPolicyService;

    // IDs de estado (constantes)
    private static final Long STATUS_PENDIENTE   = 1L;
    private static final Long STATUS_APROBADA    = 2L;
    private static final Long STATUS_RECHAZADA   = 3L;
    private static final Long STATUS_ANULADA     = 4L;
    private static final Long STATUS_FINALIZADA  = 5L;
    private static final Long STATUS_REVOCADA    = 6L;

    // ======================================================================
    // CREAR RESERVA — Caso 9, 10, 11, 14, 15
    // ======================================================================

    /**
     * Crea una nueva solicitud de reserva.
     * Caso 9: Actividad extracurricular (id_course NULL)
     * Caso 10: Valida capacidad (solicitante + integrantes)
     * Caso 11: Valida feriado
     * Caso 14: Idempotency key para evitar duplicados
     * Caso 15: Valida duración > 0
     */
    @Transactional
    public ReservationResponse createReservation(ReservationCreateRequest request, Long authenticatedUserProfileId) {

        // Caso 14: Verificar idempotency_key antes de procesar
        if (request.getIdempotencyKey() != null && !request.getIdempotencyKey().isBlank()) {
            Optional<ReservationEntity> existing = reservationRepository.findByIdempotencyKey(request.getIdempotencyKey());
            if (existing.isPresent()) {
                log.info("Solicitud duplicada detectada con idempotency_key: {}", request.getIdempotencyKey());
                return toResponse(existing.get());
            }
        }

        // Caso 15: Validar start < end y duración > 0
        validateDateRange(request.getStartDatetime(), request.getEndDatetime());

        // Caso 11: Validar feriados
        if (holidayPolicyService.isHoliday(request.getStartDatetime().toLocalDate()) ||
            holidayPolicyService.isHoliday(request.getEndDatetime().toLocalDate())) {
            throw new IllegalArgumentException(
                "No se pueden realizar reservas en días feriados. Fecha solicitada: " +
                request.getStartDatetime().toLocalDate());
        }

        // Validar que el espacio académico existe en MS-ENVIRONMENT
        AcademicSpaceClientDto space = getAcademicSpaceOrThrow(request.getIdAcademicSpace());

        // Caso 10: Validar capacidad (solicitante = 1 + integrantes)
        int memberCount = request.getMemberIds() != null ? request.getMemberIds().size() : 0;
        int totalOccupants = 1 + memberCount; // 1 = solicitante
        if (totalOccupants > space.getCapacity()) {
            throw new IllegalArgumentException(
                "El total de asistentes (" + totalOccupants + ") supera la capacidad del espacio (" +
                space.getCapacity() + "). Solicitante: 1, Integrantes: " + memberCount);
        }

        // Caso 9: Validar curso si se proporcionó (id_course nullable)
        if (request.getIdCourse() != null) {
            try {
                courseClient.getCourseById(request.getIdCourse());
            } catch (FeignException.NotFound e) {
                throw new RuntimeException("Curso no encontrado con id: " + request.getIdCourse());
            }
        }

        // Validar solicitante existe en MS-USER
        try {
            userProfileClient.getUserProfileById(authenticatedUserProfileId);
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("Perfil de usuario no encontrado con id: " + authenticatedUserProfileId);
        }

        // Validar cada miembro si se enviaron
        if (request.getMemberIds() != null) {
            for (Long memberId : request.getMemberIds()) {
                try {
                    userProfileClient.getUserProfileById(memberId);
                } catch (FeignException.NotFound e) {
                    throw new RuntimeException("Integrante no encontrado con id_user_profile: " + memberId);
                }
            }
        }

        // Obtener estado PENDIENTE
        ReservationStatusEntity statusPendiente = getStatusOrThrow(STATUS_PENDIENTE);

        // Crear la reserva
        ReservationEntity reservation = ReservationEntity.builder()
                .startDatetime(request.getStartDatetime())
                .endDatetime(request.getEndDatetime())
                .reason(request.getReason())
                .description(request.getDescription())
                .requestedAt(LocalDateTime.now())
                .idUserProfile(authenticatedUserProfileId)
                .idAcademicSpace(request.getIdAcademicSpace())
                .idCourse(request.getIdCourse())
                .idSchedule(null) // Se asignará al aprobar
                .status(statusPendiente)
                .idempotencyKey(request.getIdempotencyKey())
                .build();

        reservation = reservationRepository.save(reservation);

        // Agregar integrantes
        if (request.getMemberIds() != null && !request.getMemberIds().isEmpty()) {
            final Long reservationId = reservation.getIdReservation();
            final ReservationEntity finalReservation = reservation;
            List<ReservationMemberEntity> members = request.getMemberIds().stream()
                    .map(memberId -> ReservationMemberEntity.builder()
                            .reservation(finalReservation)
                            .idUserProfile(memberId)
                            .build())
                    .collect(Collectors.toList());
            memberRepository.saveAll(members);
        }

        // Registrar en log (estado inicial)
        saveLog(reservation, null, statusPendiente,
                "Solicitud de reserva registrada", authenticatedUserProfileId);

        log.info("Reserva creada con id={} para user={}", reservation.getIdReservation(), authenticatedUserProfileId);
        return toResponse(reservation);
    }

    // ======================================================================
    // CONSULTAS — Caso 16, 12
    // ======================================================================

    public ReservationResponse getReservationById(Long id) {
        return toResponse(getReservationOrThrow(id));
    }

    public List<ReservationResponse> getReservationsByStudent(Long idUserProfile) {
        return reservationRepository.findByIdUserProfile(idUserProfile)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     * Historial con filtros — Caso 16
     */
    public List<ReservationResponse> getStudentHistory(
            Long idUserProfile, Long idStatus, LocalDateTime startFrom,
            LocalDateTime endTo, Long idAcademicSpace, Long idCourse) {

        return reservationRepository.findStudentHistory(
                idUserProfile, idStatus, startFrom, endTo, idAcademicSpace, idCourse)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ======================================================================
    // CANCELAR — Caso 12, 13
    // ======================================================================

    /**
     * Cancela (anula) una reserva.
     * Caso 12: Solo el dueño puede cancelar su propia reserva.
     * Caso 13: Solo si está en estado PENDIENTE.
     */
    @Transactional
    public ReservationResponse cancelReservation(Long id, StatusChangeRequest request,
            Long authenticatedUserProfileId, boolean isAdmin) {

        ReservationEntity reservation = getReservationOrThrow(id);

        // Caso 12: Verificar propiedad (solo dueño o admin puede cancelar)
        if (!isAdmin && !reservation.getIdUserProfile().equals(authenticatedUserProfileId)) {
            throw new AccessDeniedException(
                "No tiene permiso para cancelar esta reserva. Solo el solicitante puede cancelar su propia reserva.");
        }

        // Caso 13: Solo se puede cancelar si está PENDIENTE
        if (!reservation.getStatus().getIdStatus().equals(STATUS_PENDIENTE)) {
            throw new IllegalStateException(
                "Solo se pueden anular reservas en estado Pendiente. Estado actual: " +
                reservation.getStatus().getName());
        }

        ReservationStatusEntity previousStatus = reservation.getStatus();
        ReservationStatusEntity newStatus = getStatusOrThrow(STATUS_ANULADA);

        reservation.setStatus(newStatus);
        reservation = reservationRepository.save(reservation);

        saveLog(reservation, previousStatus, newStatus,
                request != null && request.getChangeReason() != null ?
                        request.getChangeReason() : "Reserva anulada por el solicitante",
                authenticatedUserProfileId);

        log.info("Reserva {} anulada por user={}", id, authenticatedUserProfileId);
        return toResponse(reservation);
    }

    // ======================================================================
    // APROBAR — Caso 17, 19, 23 con @Transactional para race condition
    // ======================================================================

    /**
     * Aprueba una reserva.
     * Caso 17: Crea bloqueo en MS-SCHEDULE, guarda id_schedule.
     * Caso 19: Re-valida disponibilidad al momento de aprobar (race condition).
     * Caso 23: Solo ADMIN puede aprobar.
     * @Transactional asegura atomicidad del proceso completo.
     */
    @Transactional
    public ReservationResponse approveReservation(Long id, StatusChangeRequest request,
            Long authenticatedUserProfileId) {

        ReservationEntity reservation = getReservationOrThrow(id);

        if (!reservation.getStatus().getIdStatus().equals(STATUS_PENDIENTE)) {
            throw new IllegalStateException(
                "Solo se pueden aprobar reservas en estado Pendiente. Estado actual: " +
                reservation.getStatus().getName());
        }

        // Caso 19: Re-validar disponibilidad al momento de aprobar (race condition protection)
        boolean hasConflict = reservationRepository.existsApprovedOverlap(
                reservation.getIdAcademicSpace(),
                reservation.getStartDatetime(),
                reservation.getEndDatetime(),
                id); // Excluimos esta misma reserva

        if (hasConflict) {
            log.warn("CONFLICT: Reserva {} no puede aprobarse. El espacio {} ya fue ocupado.",
                    id, reservation.getIdAcademicSpace());
            throw new IllegalStateException(
                "CONFLICT: El espacio académico ya no está disponible para el horario solicitado. " +
                "Otro administrador aprobó una reserva para ese horario.");
        }

        // Caso 17: Bloquear horario en MS-SCHEDULE
        ReservationBlockClientDto blockRequest = ReservationBlockClientDto.builder()
                .idAcademicSpace(reservation.getIdAcademicSpace())
                .date(reservation.getStartDatetime().toLocalDate())
                .startTime(reservation.getStartDatetime().toLocalTime())
                .endTime(reservation.getEndDatetime().toLocalTime())
                .idReservation(id)
                .build();

        try {
            ResponseEntity<ReservationBlockResponseDto> blockResponse =
                    scheduleClient.createReservationBlock(blockRequest);

            if (blockResponse.getStatusCode().value() == 409) {
                throw new IllegalStateException(
                    "CONFLICT: El horario ya está bloqueado en el sistema de horarios. " +
                    "No se puede aprobar la reserva.");
            }

            if (blockResponse.getBody() != null && blockResponse.getBody().getIdReservationBlock() != null) {
                reservation.setIdSchedule(blockResponse.getBody().getIdReservationBlock());
            }
        } catch (FeignException.Conflict e) {
            throw new IllegalStateException(
                "CONFLICT: El horario ya está bloqueado en el sistema de horarios.");
        } catch (FeignException e) {
            log.error("Error al bloquear horario en MS-SCHEDULE: {}", e.getMessage());
            throw new RuntimeException("No se pudo bloquear el horario en el sistema de horarios.");
        }

        ReservationStatusEntity previousStatus = reservation.getStatus();
        ReservationStatusEntity newStatus = getStatusOrThrow(STATUS_APROBADA);

        reservation.setStatus(newStatus);
        reservation = reservationRepository.save(reservation);

        saveLog(reservation, previousStatus, newStatus,
                request != null && request.getChangeReason() != null ?
                        request.getChangeReason() : "Reserva aprobada por administrador",
                authenticatedUserProfileId);

        log.info("Reserva {} aprobada por admin={}, schedule_block={}", 
                id, authenticatedUserProfileId, reservation.getIdSchedule());
        return toResponse(reservation);
    }

    // ======================================================================
    // RECHAZAR — Caso 18
    // ======================================================================

    /**
     * Rechaza una reserva con motivo obligatorio.
     * Caso 18: changeReason es obligatorio al rechazar.
     */
    @Transactional
    public ReservationResponse rejectReservation(Long id, StatusChangeRequest request,
            Long authenticatedUserProfileId) {

        if (request == null || request.getChangeReason() == null || request.getChangeReason().isBlank()) {
            throw new IllegalArgumentException("El motivo de rechazo (changeReason) es obligatorio.");
        }

        ReservationEntity reservation = getReservationOrThrow(id);

        if (!reservation.getStatus().getIdStatus().equals(STATUS_PENDIENTE)) {
            throw new IllegalStateException(
                "Solo se pueden rechazar reservas en estado Pendiente. Estado actual: " +
                reservation.getStatus().getName());
        }

        ReservationStatusEntity previousStatus = reservation.getStatus();
        ReservationStatusEntity newStatus = getStatusOrThrow(STATUS_RECHAZADA);

        reservation.setStatus(newStatus);
        reservation = reservationRepository.save(reservation);

        saveLog(reservation, previousStatus, newStatus, request.getChangeReason(), authenticatedUserProfileId);

        log.info("Reserva {} rechazada por admin={}", id, authenticatedUserProfileId);
        return toResponse(reservation);
    }

    // ======================================================================
    // FINALIZAR
    // ======================================================================

    @Transactional
    public ReservationResponse finishReservation(Long id, StatusChangeRequest request,
            Long authenticatedUserProfileId) {

        ReservationEntity reservation = getReservationOrThrow(id);

        if (!reservation.getStatus().getIdStatus().equals(STATUS_APROBADA)) {
            throw new IllegalStateException(
                "Solo se pueden finalizar reservas en estado Aprobada. Estado actual: " +
                reservation.getStatus().getName());
        }

        ReservationStatusEntity previousStatus = reservation.getStatus();
        ReservationStatusEntity newStatus = getStatusOrThrow(STATUS_FINALIZADA);

        reservation.setStatus(newStatus);
        reservation = reservationRepository.save(reservation);

        saveLog(reservation, previousStatus, newStatus,
                request != null && request.getChangeReason() != null ?
                        request.getChangeReason() : "Reserva finalizada",
                authenticatedUserProfileId);

        log.info("Reserva {} finalizada por admin={}", id, authenticatedUserProfileId);
        return toResponse(reservation);
    }

    // ======================================================================
    // REVOCAR — Caso 22
    // ======================================================================

    /**
     * Revoca una reserva ya aprobada.
     * Caso 22: Libera el horario en MS-SCHEDULE, registra log, motivo obligatorio.
     */
    @Transactional
    public ReservationResponse revokeReservation(Long id, StatusChangeRequest request,
            Long authenticatedUserProfileId) {

        if (request == null || request.getChangeReason() == null || request.getChangeReason().isBlank()) {
            throw new IllegalArgumentException("El motivo de revocación (changeReason) es obligatorio.");
        }

        ReservationEntity reservation = getReservationOrThrow(id);

        if (!reservation.getStatus().getIdStatus().equals(STATUS_APROBADA)) {
            throw new IllegalStateException(
                "Solo se pueden revocar reservas en estado Aprobada. Estado actual: " +
                reservation.getStatus().getName());
        }

        // Caso 22: Liberar horario en MS-SCHEDULE
        if (reservation.getIdSchedule() != null) {
            try {
                scheduleClient.releaseReservationBlock(reservation.getIdSchedule());
                log.info("Bloqueo de horario {} liberado en MS-SCHEDULE", reservation.getIdSchedule());
            } catch (FeignException e) {
                log.error("Error al liberar horario en MS-SCHEDULE: {}. Continuando revocación.", e.getMessage());
                // No bloqueamos la revocación si falla la liberación del horario
            }
        }

        ReservationStatusEntity previousStatus = reservation.getStatus();
        ReservationStatusEntity newStatus = getStatusOrThrow(STATUS_REVOCADA);

        reservation.setStatus(newStatus);
        reservation = reservationRepository.save(reservation);

        saveLog(reservation, previousStatus, newStatus, request.getChangeReason(), authenticatedUserProfileId);

        log.info("Reserva {} revocada por admin={}", id, authenticatedUserProfileId);
        return toResponse(reservation);
    }

    // ======================================================================
    // BATCH APPROVE — Caso 20, 21
    // ======================================================================

    /**
     * Aprueba múltiples reservas en lote.
     * Caso 20: Aprueba sin conflictos.
     * Caso 21: Detecta conflictos internos entre reservas del lote.
     */
    @Transactional
    public List<BatchApproveResult> batchApprove(BatchApproveRequest request, Long authenticatedUserProfileId) {

        List<BatchApproveResult> results = new ArrayList<>();
        // Rastrear qué espacios/horarios se van aprobando dentro del lote para detectar conflictos internos
        List<ReservationEntity> approvedInBatch = new ArrayList<>();

        for (Long reservationId : request.getReservationIds()) {
            try {
                ReservationEntity reservation = reservationRepository.findById(reservationId)
                        .orElse(null);

                if (reservation == null) {
                    results.add(BatchApproveResult.builder()
                            .idReservation(reservationId)
                            .result("NOT_FOUND")
                            .message("Reserva no encontrada")
                            .build());
                    continue;
                }

                // Verificar estado PENDIENTE
                if (!reservation.getStatus().getIdStatus().equals(STATUS_PENDIENTE)) {
                    results.add(BatchApproveResult.builder()
                            .idReservation(reservationId)
                            .result("INVALID_STATUS")
                            .message("La reserva no está en estado Pendiente. Estado: " +
                                    reservation.getStatus().getName())
                            .build());
                    continue;
                }

                // Caso 21: Verificar conflicto interno dentro del lote
                boolean batchConflict = approvedInBatch.stream()
                        .anyMatch(approved ->
                            approved.getIdAcademicSpace().equals(reservation.getIdAcademicSpace()) &&
                            reservation.getStartDatetime().isBefore(approved.getEndDatetime()) &&
                            reservation.getEndDatetime().isAfter(approved.getStartDatetime()));

                if (batchConflict) {
                    results.add(BatchApproveResult.builder()
                            .idReservation(reservationId)
                            .result("REJECTED_BATCH_CONFLICT")
                            .message("Conflicto de horario con otra reserva dentro del mismo lote")
                            .build());
                    continue;
                }

                // Caso 20/19: Verificar conflicto externo (reservas ya aprobadas en BD)
                boolean externalConflict = reservationRepository.existsApprovedOverlap(
                        reservation.getIdAcademicSpace(),
                        reservation.getStartDatetime(),
                        reservation.getEndDatetime(),
                        reservationId);

                if (externalConflict) {
                    results.add(BatchApproveResult.builder()
                            .idReservation(reservationId)
                            .result("REJECTED_EXTERNAL_CONFLICT")
                            .message("El espacio ya está ocupado por otra reserva aprobada")
                            .build());
                    continue;
                }

                // Aprobar: bloquear horario en MS-SCHEDULE
                ReservationBlockClientDto blockRequest = ReservationBlockClientDto.builder()
                        .idAcademicSpace(reservation.getIdAcademicSpace())
                        .date(reservation.getStartDatetime().toLocalDate())
                        .startTime(reservation.getStartDatetime().toLocalTime())
                        .endTime(reservation.getEndDatetime().toLocalTime())
                        .idReservation(reservationId)
                        .build();

                try {
                    ResponseEntity<ReservationBlockResponseDto> blockResponse =
                            scheduleClient.createReservationBlock(blockRequest);

                    if (blockResponse.getBody() != null) {
                        reservation.setIdSchedule(blockResponse.getBody().getIdReservationBlock());
                    }
                } catch (FeignException.Conflict e) {
                    results.add(BatchApproveResult.builder()
                            .idReservation(reservationId)
                            .result("REJECTED_EXTERNAL_CONFLICT")
                            .message("El horario ya está bloqueado en el sistema de horarios")
                            .build());
                    continue;
                }

                ReservationStatusEntity previousStatus = reservation.getStatus();
                ReservationStatusEntity newStatus = getStatusOrThrow(STATUS_APROBADA);

                reservation.setStatus(newStatus);
                reservationRepository.save(reservation);

                String reason = request.getChangeReason() != null ?
                        request.getChangeReason() : "Aprobación en lote por administrador";
                saveLog(reservation, previousStatus, newStatus, reason, authenticatedUserProfileId);

                approvedInBatch.add(reservation); // Registrar como aprobada en el lote

                results.add(BatchApproveResult.builder()
                        .idReservation(reservationId)
                        .result("APPROVED")
                        .message("Reserva aprobada exitosamente")
                        .build());

            } catch (Exception e) {
                log.error("Error procesando reserva {} en batch: {}", reservationId, e.getMessage());
                results.add(BatchApproveResult.builder()
                        .idReservation(reservationId)
                        .result("ERROR")
                        .message("Error al procesar: " + e.getMessage())
                        .build());
            }
        }

        log.info("Batch-approve completado: {} reservas procesadas", results.size());
        return results;
    }

    // ======================================================================
    // HISTORIAL DE LOGS
    // ======================================================================

    public List<ReservationLogResponse> getReservationLogs(Long idReservation) {
        getReservationOrThrow(idReservation); // Validar que existe
        return logRepository.findByReservation_IdReservationOrderByChangedAtAsc(idReservation)
                .stream().map(this::toLogResponse).collect(Collectors.toList());
    }

    // ======================================================================
    // MÉTODOS AUXILIARES PRIVADOS
    // ======================================================================

    private void validateDateRange(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son requeridas.");
        }
        // Caso 15: Duración > 0 minutos
        if (!start.isBefore(end)) {
            throw new IllegalArgumentException(
                "La fecha de inicio debe ser anterior a la fecha de fin. " +
                "Si son iguales, la duración sería 0 minutos.");
        }
        // No se puede reservar en el pasado
        if (start.isBefore(LocalDateTime.now().minusMinutes(5))) {
            throw new IllegalArgumentException(
                "No se puede reservar en una fecha/hora pasada.");
        }
    }

    private AcademicSpaceClientDto getAcademicSpaceOrThrow(Long idAcademicSpace) {
        try {
            return academicSpaceClient.getAcademicSpaceById(idAcademicSpace);
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("Espacio académico no encontrado con id: " + idAcademicSpace);
        } catch (FeignException e) {
            throw new RuntimeException("Error al consultar espacio académico: " + e.getMessage());
        }
    }

    private ReservationEntity getReservationOrThrow(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + id));
    }

    private ReservationStatusEntity getStatusOrThrow(Long idStatus) {
        return statusRepository.findById(idStatus)
                .orElseThrow(() -> new RuntimeException("Estado de reserva no encontrado con id: " + idStatus));
    }

    private void saveLog(ReservationEntity reservation, ReservationStatusEntity previousStatus,
            ReservationStatusEntity newStatus, String changeReason, Long idUserProfile) {

        ReservationLogEntity log = ReservationLogEntity.builder()
                .reservation(reservation)
                .previousStatus(previousStatus)
                .newStatus(newStatus)
                .changeReason(changeReason)
                .changedAt(LocalDateTime.now())
                .idUserProfile(idUserProfile)
                .build();
        logRepository.save(log);
    }

    private ReservationResponse toResponse(ReservationEntity entity) {
        return ReservationResponse.builder()
                .idReservation(entity.getIdReservation())
                .startDatetime(entity.getStartDatetime())
                .endDatetime(entity.getEndDatetime())
                .reason(entity.getReason())
                .description(entity.getDescription())
                .requestedAt(entity.getRequestedAt())
                .idUserProfile(entity.getIdUserProfile())
                .idAcademicSpace(entity.getIdAcademicSpace())
                .idCourse(entity.getIdCourse())
                .idSchedule(entity.getIdSchedule())
                .status(toStatusResponse(entity.getStatus()))
                .idempotencyKey(entity.getIdempotencyKey())
                .build();
    }

    private ReservationStatusResponse toStatusResponse(ReservationStatusEntity entity) {
        return ReservationStatusResponse.builder()
                .idStatus(entity.getIdStatus())
                .name(entity.getName())
                .isActive(entity.getIsActive())
                .build();
    }

    private ReservationLogResponse toLogResponse(ReservationLogEntity entity) {
        return ReservationLogResponse.builder()
                .idReservationLog(entity.getIdReservationLog())
                .idReservation(entity.getReservation().getIdReservation())
                .previousStatus(entity.getPreviousStatus() != null ? toStatusResponse(entity.getPreviousStatus()) : null)
                .newStatus(toStatusResponse(entity.getNewStatus()))
                .changeReason(entity.getChangeReason())
                .changedAt(entity.getChangedAt())
                .idUserProfile(entity.getIdUserProfile())
                .build();
    }
}
