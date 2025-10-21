package pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.entity.TypeHourEntity;

@Repository
public interface TypeHourJpaRepository extends JpaRepository<TypeHourEntity, Long> {
}
