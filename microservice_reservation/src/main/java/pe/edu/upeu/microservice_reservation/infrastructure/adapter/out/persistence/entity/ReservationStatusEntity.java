package pe.edu.upeu.microservice_reservation.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad JPA para reservation_status.
 * Tabla de catálogo con FK interna usada por Reservation y ReservationLog.
 */
@Entity
@Table(name = "reservation_status")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationStatusEntity {

    @Id
    @Column(name = "id_status")
    private Long idStatus;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}
