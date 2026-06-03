package pe.edu.upeu.microservice_reservation.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad JPA para la tabla reservation.
 * Las referencias a otros microservicios (id_user_profile, id_academic_space,
 * id_course, id_schedule) son referencias lógicas sin FK real, 
 * siguiendo el patrón del proyecto.
 */
@Entity
@Table(
    name = "reservation",
    uniqueConstraints = @UniqueConstraint(columnNames = "idempotency_key")
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservation")
    private Long idReservation;

    @Column(name = "start_datetime", nullable = false)
    private LocalDateTime startDatetime;

    @Column(name = "end_datetime", nullable = false)
    private LocalDateTime endDatetime;

    @Column(name = "reason", nullable = false, length = 150)
    private String reason;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "requested_at", nullable = false, updatable = false)
    private LocalDateTime requestedAt;

    /** Referencia lógica al perfil del estudiante solicitante en MS-USER */
    @Column(name = "id_user_profile", nullable = false)
    private Long idUserProfile;

    /** Referencia lógica al espacio académico en MS-ENVIRONMENT */
    @Column(name = "id_academic_space", nullable = false)
    private Long idAcademicSpace;

    /** Referencia lógica al curso en MS-COURSE. NULL si es actividad extracurricular */
    @Column(name = "id_course")
    private Long idCourse;

    /** Referencia lógica al bloqueo de horario creado en MS-SCHEDULE al aprobar */
    @Column(name = "id_schedule")
    private Long idSchedule;

    /** FK interna hacia reservation_status */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_status", nullable = false)
    private ReservationStatusEntity status;

    /**
     * Clave de idempotencia para evitar duplicados por doble clic.
     * Si llega la misma clave, no se crea reserva duplicada.
     */
    @Column(name = "idempotency_key", unique = true, length = 100)
    private String idempotencyKey;

    @PrePersist
    protected void onCreate() {
        if (requestedAt == null) {
            requestedAt = LocalDateTime.now();
        }
    }
}
