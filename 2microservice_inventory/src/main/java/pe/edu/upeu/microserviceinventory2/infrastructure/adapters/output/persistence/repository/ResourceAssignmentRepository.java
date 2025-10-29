package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.ResourceAssignmentEntity;

import java.util.List;

public interface ResourceAssignmentRepository extends JpaRepository<ResourceAssignmentEntity, Long> {
    List<ResourceAssignmentEntity> findByIdResource(Long idResource);
    List<ResourceAssignmentEntity> findByIdAcademicSpace(Long idAcademicSpace);
}
