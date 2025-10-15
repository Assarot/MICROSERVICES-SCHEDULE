package pe.edu.upeu.microserviceenviroment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upeu.microserviceenviroment.application.ports.input.BuildingServicePort;
import pe.edu.upeu.microserviceenviroment.application.ports.output.BuildingPersistancePort;
import pe.edu.upeu.microserviceenviroment.domain.exception.BuildingNotFoundException;
import pe.edu.upeu.microserviceenviroment.domain.model.Building;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuildingService implements BuildingServicePort {

    private final BuildingPersistancePort buildingPersistancePort;

    @Override
    public Building findById(Long id) {
        return buildingPersistancePort.findById(id)
                .orElseThrow(BuildingNotFoundException::new);
    }

    @Override
    public List<Building> findAll() {
        return buildingPersistancePort.findAll();
    }

    @Override
    public Building save(Building building) {
        return buildingPersistancePort.save(building);
    }

    @Override
    public Building update(Long id, Building building) {
        return buildingPersistancePort.findById(id)
                .map(saveBuilding -> {
                    saveBuilding.setName(building.getName());
                    saveBuilding.setIsActive(building.getIsActive());
                    return buildingPersistancePort.save(saveBuilding);
                }).orElseThrow(BuildingNotFoundException::new);
    }

    @Override
    public void deleteById(Long id) {
        if (buildingPersistancePort.findById(id).isEmpty()) {
            throw new BuildingNotFoundException();
        }
        buildingPersistancePort.deleteById(id);
    }
}
