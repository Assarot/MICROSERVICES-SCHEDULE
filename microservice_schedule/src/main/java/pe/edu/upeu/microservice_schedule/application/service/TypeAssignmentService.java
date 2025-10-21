package pe.edu.upeu.microservice_schedule.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.microservice_schedule.domain.model.TypeAssignment;
import pe.edu.upeu.microservice_schedule.domain.port.in.TypeAssignmentUseCase;
import pe.edu.upeu.microservice_schedule.domain.port.out.TypeAssignmentRepositoryPort;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TypeAssignmentService implements TypeAssignmentUseCase {
    
    private final TypeAssignmentRepositoryPort repositoryPort;

    @Override
    public TypeAssignment create(TypeAssignment typeAssignment) {
        return repositoryPort.save(typeAssignment);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypeAssignment> findById(Long id) {
        return repositoryPort.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TypeAssignment> findAll() {
        return repositoryPort.findAll();
    }

    @Override
    public TypeAssignment update(Long id, TypeAssignment typeAssignment) {
        if (!repositoryPort.existsById(id)) {
            throw new RuntimeException("TypeAssignment not found with id: " + id);
        }
        typeAssignment.setIdSchedule(id);
        return repositoryPort.save(typeAssignment);
    }

    @Override
    public void delete(Long id) {
        if (!repositoryPort.existsById(id)) {
            throw new RuntimeException("TypeAssignment not found with id: " + id);
        }
        repositoryPort.deleteById(id);
    }
}
