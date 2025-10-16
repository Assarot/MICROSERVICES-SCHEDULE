package pe.edu.upeu.microservice_schedule.domain.port.in;

import pe.edu.upeu.microservice_schedule.domain.model.TypeAssignment;

import java.util.List;
import java.util.Optional;

public interface TypeAssignmentUseCase {
    TypeAssignment create(TypeAssignment typeAssignment);
    Optional<TypeAssignment> findById(Long id);
    List<TypeAssignment> findAll();
    TypeAssignment update(Long id, TypeAssignment typeAssignment);
    void delete(Long id);
}
