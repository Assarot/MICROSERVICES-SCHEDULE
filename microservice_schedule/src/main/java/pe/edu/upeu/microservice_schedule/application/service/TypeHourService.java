package pe.edu.upeu.microservice_schedule.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.microservice_schedule.domain.model.TypeHour;
import pe.edu.upeu.microservice_schedule.domain.port.in.TypeHourUseCase;
import pe.edu.upeu.microservice_schedule.domain.port.out.TypeHourRepositoryPort;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TypeHourService implements TypeHourUseCase {
    
    private final TypeHourRepositoryPort repositoryPort;

    @Override
    public TypeHour create(TypeHour typeHour) {
        return repositoryPort.save(typeHour);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypeHour> findById(Long id) {
        return repositoryPort.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TypeHour> findAll() {
        return repositoryPort.findAll();
    }

    @Override
    public TypeHour update(Long id, TypeHour typeHour) {
        if (!repositoryPort.existsById(id)) {
            throw new RuntimeException("TypeHour not found with id: " + id);
        }
        typeHour.setIdTypeHour(id);
        return repositoryPort.save(typeHour);
    }

    @Override
    public void delete(Long id) {
        if (!repositoryPort.existsById(id)) {
            throw new RuntimeException("TypeHour not found with id: " + id);
        }
        repositoryPort.deleteById(id);
    }
}
