package pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Entidad para bloqueos de horario generados por reservas.
 * Separa los horarios académicos regulares de los bloqueos por reserva.
 */
@Entity
@Table(name = "reservation_block")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationBlockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservation_block")
    private Long idReservationBlock;

    @Column(name = "id_academic_space", nullable = false)
    private Long idAcademicSpace;

    @Column(name = "block_date", nullable = false)
    private LocalDate blockDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    /**
     * Referencia lógica hacia la reserva en MS-RESERVATION.
     * No es FK real porque es entre microservicios.
     */
    @Column(name = "id_reservation")
    private Long idReservation;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}
