package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microserviceenviroment.application.ports.output.AcademicSpacePersistancePort;
import pe.edu.upeu.microserviceenviroment.domain.model.AcademicSpace;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.mapper.AcademicSpacePersistenceMapper;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.repository.AcademicSpaceRepository;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.entity.AcademicSpaceEntity;

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
        if (academicSpace.getIdAcademicSpace() != null) {
            AcademicSpaceEntity existing = repository.findById(academicSpace.getIdAcademicSpace())
                    .orElseGet(() -> mapper.toAcademicSpaceEntity(academicSpace));

            // Update scalar fields
            existing.setSpaceName(academicSpace.getSpaceName());
            existing.setObservation(academicSpace.getObservation());
            existing.setLocation(academicSpace.getLocation());
            existing.setCapacity(academicSpace.getCapacity());

            // Update associations using mapper helper methods (they set only the id)
            existing.setTypeAcademicSpaceEntity(mapper.mapTypeAcademicSpaceEntity(academicSpace.getTypeAcademicSpace()));
            existing.setStateEntity(mapper.mapStateToEntity(academicSpace.getState()));
            existing.setFloorEntity(mapper.mapFloorToEntity(academicSpace.getFloor()));

            AcademicSpaceEntity saved = repository.save(existing);
            AcademicSpaceEntity reloaded = repository.findById(saved.getIdAcademicSpace()).orElse(saved);
            return mapper.toAcademicSpace(reloaded);
        }

        AcademicSpaceEntity entity = mapper.toAcademicSpaceEntity(academicSpace);
        AcademicSpaceEntity savedEntity = repository.save(entity);
        AcademicSpaceEntity reloaded = repository.findById(savedEntity.getIdAcademicSpace()).orElse(savedEntity);
        return mapper.toAcademicSpace(reloaded);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
