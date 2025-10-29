package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microserviceenviroment.application.ports.output.BuildingPersistancePort;
import pe.edu.upeu.microserviceenviroment.domain.model.Building;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.mapper.BuildingPersistenceMapper;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.repository.BuildingRepository;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.entity.BuildingEntity;

import java.util.List;
import java.util.Optional;
@Component
@RequiredArgsConstructor
public class BuildingPersistenceAdapter implements BuildingPersistancePort {

    private final BuildingPersistenceMapper mapper;
    private final BuildingRepository repository;

    @Override
    public Optional<Building> findById(Long id) {
        return repository.findById(id).map(mapper::toBuilding);
    }

    @Override
    public List<Building> findAll() {
        return mapper.toBuildingList(repository.findAll());
    }

    @Override
    public Building save(Building building) {
        if (building.getIdBuilding() != null) {
            BuildingEntity existing = repository.findById(building.getIdBuilding())
                    .orElseGet(() -> mapper.toBuildingEntity(building));
            existing.setName(building.getName());
            existing.setIsActive(building.getIsActive());
            BuildingEntity saved = repository.save(existing);
            BuildingEntity reloaded = repository.findById(saved.getIdBuilding()).orElse(saved);
            return mapper.toBuilding(reloaded);
        }

        BuildingEntity entity = mapper.toBuildingEntity(building);
        BuildingEntity savedEntity = repository.save(entity);
        BuildingEntity reloaded = repository.findById(savedEntity.getIdBuilding()).orElse(savedEntity);
        return mapper.toBuilding(reloaded);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
