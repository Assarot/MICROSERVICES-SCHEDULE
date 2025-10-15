package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microserviceenviroment.application.ports.output.AcademicSpacePersistancePort;
import pe.edu.upeu.microserviceenviroment.domain.model.AcademicSpace;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.mapper.AcademicSpacePersistenceMapper;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.repository.AcademicSpaceRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AcademicSpacePersistenceAdapter implements AcademicSpacePersistancePort {

    private final AcademicSpaceRepository repository;
    private final AcademicSpacePersistenceMapper mapper;

    @Override
    public Optional<AcademicSpace> findById(Long id) {
        return repository.findById(id).map(mapper::toAcademicSpace);
    }

    @Override
    public List<AcademicSpace> findAll() {
        return mapper.toAcademicSpaceList(repository.findAll());
    }

    @Override
    public AcademicSpace save(AcademicSpace academicSpace) {
        return mapper.toAcademicSpace(repository.save(mapper.toAcademicSpaceEntity(academicSpace)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
