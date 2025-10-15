package pe.edu.upeu.microserviceenviroment.application.ports.input;

import pe.edu.upeu.microserviceenviroment.domain.model.Building;

import java.util.List;

public interface BuildingServicePort {
    Building findById(Long id);
    List<Building> findAll();
    Building save(Building building);
    Building update(Long id,Building building);
    void deleteById(Long id);
}
