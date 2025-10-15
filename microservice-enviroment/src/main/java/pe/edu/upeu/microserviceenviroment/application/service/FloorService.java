package pe.edu.upeu.microserviceenviroment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upeu.microserviceenviroment.application.ports.input.FloorServicePort;
import pe.edu.upeu.microserviceenviroment.application.ports.output.FloorPersistancePort;
import pe.edu.upeu.microserviceenviroment.domain.exception.FloorNotFoundException;
import pe.edu.upeu.microserviceenviroment.domain.model.Floor;

import java.util.List;
@Service
@RequiredArgsConstructor
public class FloorService implements FloorServicePort {

    private final FloorPersistancePort floorPersistancePort;

    @Override
    public Floor findById(Long id) {
        return floorPersistancePort.findById(id)
                .orElseThrow(FloorNotFoundException::new);
    }

    @Override
    public List<Floor> findAll() {
        return floorPersistancePort.findAll();
    }

    @Override
    public Floor save(Floor floor) {
        return floorPersistancePort.save(floor);
    }

    @Override
    public Floor update(Long id, Floor floor) {
        return floorPersistancePort.findById(id)
                .map(savedFloor -> {
                    savedFloor.setFloorNumber(floor.getFloorNumber());
                    savedFloor.setIsActive(floor.getIsActive());
                    savedFloor.setBuilding(floor.getBuilding());
                    return floorPersistancePort.save(savedFloor);
                }).orElseThrow(FloorNotFoundException::new);
    }

    @Override
    public void deleteById(Long id) {
        if (floorPersistancePort.findById(id).isEmpty()) {
            throw new FloorNotFoundException();
        }
        floorPersistancePort.deleteById(id);
    }
}
