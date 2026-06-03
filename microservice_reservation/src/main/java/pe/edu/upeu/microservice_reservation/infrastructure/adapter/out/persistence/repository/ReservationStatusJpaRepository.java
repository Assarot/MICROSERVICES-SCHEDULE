package pe.edu.upeu.microservice_reservation.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.microservice_reservation.infrastructure.adapter.out.persistence.entity.ReservationStatusEntity;

import java.util.Optional;

@Repository
public interface ReservationStatusJpaRepository extends JpaRepository<ReservationStatusEntity, Long> {

    Optional<ReservationStatusEntity> findByName(String name);
}
