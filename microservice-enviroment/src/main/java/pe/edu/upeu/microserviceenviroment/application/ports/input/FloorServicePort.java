package pe.edu.upeu.microserviceenviroment.application.ports.input;

import pe.edu.upeu.microserviceenviroment.domain.model.Floor;

import java.util.List;

public interface FloorServicePort {
    Floor findById(Long id);
    List<Floor> findAll();
    Floor save(Floor floor);
    Floor update(Long id,Floor floor);
    void deleteById(Long id);
}
