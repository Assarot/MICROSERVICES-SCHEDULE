package pe.edu.upeu.microservice_reservation.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.microservice_reservation.infrastructure.adapter.out.persistence.entity.ReservationMemberEntity;

import java.util.List;

@Repository
public interface ReservationMemberJpaRepository extends JpaRepository<ReservationMemberEntity, Long> {

    List<ReservationMemberEntity> findByReservation_IdReservation(Long idReservation);

    boolean existsByReservation_IdReservationAndIdUserProfile(Long idReservation, Long idUserProfile);

    int countByReservation_IdReservation(Long idReservation);
}
