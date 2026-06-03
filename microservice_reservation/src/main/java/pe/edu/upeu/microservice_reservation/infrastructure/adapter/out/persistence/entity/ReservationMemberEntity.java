package pe.edu.upeu.microservice_reservation.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad JPA para reservation_member.
 * FK real hacia reservation (misma base de datos).
 * id_user_profile es referencia lógica hacia MS-USER.
 */
@Entity
@Table(name = "reservation_member")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservation_member")
    private Long idReservationMember;

    /** FK interna hacia reservation */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reservation", nullable = false)
    private ReservationEntity reservation;

    /** Referencia lógica hacia MS-USER */
    @Column(name = "id_user_profile", nullable = false)
    private Long idUserProfile;
}
