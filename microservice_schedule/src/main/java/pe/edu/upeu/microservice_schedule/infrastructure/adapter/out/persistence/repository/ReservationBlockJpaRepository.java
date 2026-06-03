package pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.entity.ReservationBlockEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservationBlockJpaRepository extends JpaRepository<ReservationBlockEntity, Long> {

    List<ReservationBlockEntity> findByIdAcademicSpaceAndBlockDateAndIsActiveTrue(
            Long idAcademicSpace, LocalDate blockDate);

    /**
     * Verifica si existe un bloqueo activo que se solape con el rango dado
     * para un espacio académico en una fecha específica.
     */
    @Query("SELECT COUNT(rb) > 0 FROM ReservationBlockEntity rb " +
           "WHERE rb.idAcademicSpace = :idAcademicSpace " +
           "AND rb.blockDate = :date " +
           "AND rb.isActive = true " +
           "AND rb.startTime < :endTime " +
           "AND rb.endTime > :startTime")
    boolean existsOverlappingBlock(
            @Param("idAcademicSpace") Long idAcademicSpace,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

    List<ReservationBlockEntity> findByIdAcademicSpaceAndIsActiveTrue(Long idAcademicSpace);
}
