package pe.edu.upeu.microservice_reservation.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.microservice_reservation.infrastructure.adapter.out.persistence.entity.ReservationEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {

    Optional<ReservationEntity> findByIdempotencyKey(String idempotencyKey);

    List<ReservationEntity> findByIdUserProfile(Long idUserProfile);

    /**
     * Consulta el historial de reservas de un estudiante con filtros opcionales.
     */
    @Query("SELECT r FROM ReservationEntity r " +
           "WHERE r.idUserProfile = :idUserProfile " +
           "AND (:idStatus IS NULL OR r.status.idStatus = :idStatus) " +
           "AND (:startFrom IS NULL OR r.startDatetime >= :startFrom) " +
           "AND (:endTo IS NULL OR r.endDatetime <= :endTo) " +
           "AND (:idAcademicSpace IS NULL OR r.idAcademicSpace = :idAcademicSpace) " +
           "AND (:idCourse IS NULL OR r.idCourse = :idCourse) " +
           "ORDER BY r.requestedAt DESC")
    List<ReservationEntity> findStudentHistory(
            @Param("idUserProfile") Long idUserProfile,
            @Param("idStatus") Long idStatus,
            @Param("startFrom") LocalDateTime startFrom,
            @Param("endTo") LocalDateTime endTo,
            @Param("idAcademicSpace") Long idAcademicSpace,
            @Param("idCourse") Long idCourse);

    /**
     * Verifica solapamiento de reservas APROBADAS para un espacio académico.
     * Fórmula: start < existingEnd AND end > existingStart
     */
    @Query("SELECT COUNT(r) > 0 FROM ReservationEntity r " +
           "WHERE r.idAcademicSpace = :idAcademicSpace " +
           "AND r.status.idStatus = 2 " +
           "AND r.startDatetime < :endDatetime " +
           "AND r.endDatetime > :startDatetime " +
           "AND (:excludeId IS NULL OR r.idReservation <> :excludeId)")
    boolean existsApprovedOverlap(
            @Param("idAcademicSpace") Long idAcademicSpace,
            @Param("startDatetime") LocalDateTime startDatetime,
            @Param("endDatetime") LocalDateTime endDatetime,
            @Param("excludeId") Long excludeId);

    /**
     * Obtiene reservas aprobadas para un espacio en un rango de fechas.
     */
    @Query("SELECT r FROM ReservationEntity r " +
           "WHERE r.idAcademicSpace = :idAcademicSpace " +
           "AND r.status.idStatus = 2 " +
           "AND r.startDatetime < :endDatetime " +
           "AND r.endDatetime > :startDatetime")
    List<ReservationEntity> findApprovedBySpaceAndRange(
            @Param("idAcademicSpace") Long idAcademicSpace,
            @Param("startDatetime") LocalDateTime startDatetime,
            @Param("endDatetime") LocalDateTime endDatetime);
}
