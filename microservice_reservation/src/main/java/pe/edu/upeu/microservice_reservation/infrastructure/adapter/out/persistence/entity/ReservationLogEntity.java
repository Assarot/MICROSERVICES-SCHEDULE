package pe.edu.upeu.microservice_reservation.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad JPA para reservation_log.
 * Guarda el historial completo de cambios de estado de una reserva.
 * Reemplaza campos como approved_at, rejected_at, cancelled_at, etc.
 */
@Entity
@Table(name = "reservation_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservation_log")
    private Long idReservationLog;

    /** FK interna hacia reservation */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reservation", nullable = false)
    private ReservationEntity reservation;

    /** FK interna hacia reservation_status (estado anterior, NULL en primer registro) */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_previous_status")
    private ReservationStatusEntity previousStatus;

    /** FK interna hacia reservation_status (estado nuevo) */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_new_status", nullable = false)
    private ReservationStatusEntity newStatus;

    @Column(name = "change_reason", columnDefinition = "TEXT")
    private String changeReason;

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;

    /** Referencia lógica al usuario que realizó el cambio en MS-USER */
    @Column(name = "id_user_profile")
    private Long idUserProfile;

    @PrePersist
    protected void onCreate() {
        if (changedAt == null) {
            changedAt = LocalDateTime.now();
        }
    }
}
