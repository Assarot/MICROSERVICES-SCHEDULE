package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.entity.TypeAcademicSpaceEntity;

public interface TypeAcademicSpaceRepository extends JpaRepository<TypeAcademicSpaceEntity, Long> {
}
