package pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.entity.ScheduleEntity;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface ScheduleJpaRepository extends JpaRepository<ScheduleEntity, Long> {
    List<ScheduleEntity> findByIdAcademicSpaceAndIdWeekName(Long idAcademicSpace, Long idWeekName);

    @Query("SELECT DISTINCT s.idAcademicSpace FROM ScheduleEntity s " +
           "WHERE s.idWeekName = :idWeekName " +
           "AND s.startTime < :endTime " +
           "AND s.endTime > :startTime " +
           "AND s.idAcademicSpace IS NOT NULL")
    List<Long> findOccupiedSpaceIds(@Param("idWeekName") Long idWeekName,
                                    @Param("startTime") LocalTime startTime,
                                    @Param("endTime") LocalTime endTime);
}
