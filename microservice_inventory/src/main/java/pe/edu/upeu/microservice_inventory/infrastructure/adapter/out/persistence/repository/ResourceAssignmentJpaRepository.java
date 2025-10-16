package pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.microservice_inventory.infrastructure.adapter.out.persistence.entity.ResourceAssignmentEntity;

import java.util.List;

@Repository
public interface ResourceAssignmentJpaRepository extends JpaRepository<ResourceAssignmentEntity, Long> {
    List<ResourceAssignmentEntity> findByIdResource(Long resourceId);
    List<ResourceAssignmentEntity> findByIdAcademicSpace(Long academicSpaceId);
}
