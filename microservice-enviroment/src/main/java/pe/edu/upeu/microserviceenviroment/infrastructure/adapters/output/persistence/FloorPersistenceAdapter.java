package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microserviceenviroment.application.ports.output.FloorPersistancePort;
import pe.edu.upeu.microserviceenviroment.domain.model.Floor;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.entity.FloorEntity;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.mapper.FloorPersistenceMapper;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.repository.BuildingRepository;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.repository.FloorRepository;

import java.util.List;
import java.util.Optional;
@Component
@RequiredArgsConstructor
public class FloorPersistenceAdapter implements FloorPersistancePort {

    private final FloorPersistenceMapper mapper;
    private final FloorRepository repository;

    @Override
    public Optional<Floor> findById(Long id) {
        return repository.findById(id).map(mapper::toFloor);
    }

    @Override
    public List<Floor> findAll() {
        return mapper.toFloorList(repository.findAll());
    }

    @Override
    public List<Floor> findByBuildingId(Long buildingId) {
        List<FloorEntity> entities = repository.findByBuildingEntity_IdBuilding(buildingId);
        return mapper.toFloorList(entities);
    }

    @Override
    public Floor save(Floor floor) {
        // If we have an ID, update existing entity to preserve collections (avoid orphan issues)
        if (floor.getIdFloor() != null) {
            FloorEntity existing = repository.findById(floor.getIdFloor())
                    .orElseGet(() -> mapper.toFloorEntity(floor));
            // Update scalar fields
            existing.setFloorNumber(floor.getFloorNumber());
            existing.setIsActive(floor.getIsActive());
            // Update relationship (keep collection as-is)
            existing.setBuildingEntity(mapper.mapBuildingToEntity(floor.getBuilding()));
            FloorEntity saved = repository.save(existing);
            FloorEntity reloaded = repository.findById(saved.getIdFloor()).orElse(saved);
            return mapper.toFloor(reloaded);
        }
        // Create new entity
        FloorEntity entity = mapper.toFloorEntity(floor);
        FloorEntity savedEntity = repository.save(entity);
        FloorEntity reloaded = repository.findById(savedEntity.getIdFloor()).orElse(savedEntity);
        return mapper.toFloor(reloaded);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
