package pe.edu.upeu.microserviceenviroment.application.ports.output;

import pe.edu.upeu.microserviceenviroment.domain.model.Building;

import java.util.List;
import java.util.Optional;

public interface BuildingPersistancePort {
    Optional<Building> findById(Long id);
    List<Building> findAll();
    Building save(Building building);
    void deleteById(Long id);
}
