package pe.edu.upeu.microservice_reservation.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.microservice_reservation.infrastructure.adapter.out.persistence.entity.ReservationLogEntity;

import java.util.List;

@Repository
public interface ReservationLogJpaRepository extends JpaRepository<ReservationLogEntity, Long> {

    List<ReservationLogEntity> findByReservation_IdReservationOrderByChangedAtAsc(Long idReservation);
}
