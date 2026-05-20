package pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.entity.ScheduleEntity;
import java.util.List;

@Repository
public interface ScheduleJpaRepository extends JpaRepository<ScheduleEntity, Long> {
	List<ScheduleEntity> findByIdAcademicSpaceAndIdWeekName(Long idAcademicSpace, Long idWeekName);
}
