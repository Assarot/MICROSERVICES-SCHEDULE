package pe.edu.upeu.microserviceenviroment.application.ports.output;

import pe.edu.upeu.microserviceenviroment.domain.model.Floor;

import java.util.List;
import java.util.Optional;

public interface FloorPersistancePort {
    Optional<Floor> findById(Long id);
    List<Floor> findAll();
    Floor save(Floor floor);
    void deleteById(Long id);
}
