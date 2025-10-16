package pe.edu.upeu.microservice_schedule.domain.port.out;

import pe.edu.upeu.microservice_schedule.domain.model.TypeAssignment;

import java.util.List;
import java.util.Optional;

public interface TypeAssignmentRepositoryPort {
    TypeAssignment save(TypeAssignment typeAssignment);
    Optional<TypeAssignment> findById(Long id);
    List<TypeAssignment> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
}
