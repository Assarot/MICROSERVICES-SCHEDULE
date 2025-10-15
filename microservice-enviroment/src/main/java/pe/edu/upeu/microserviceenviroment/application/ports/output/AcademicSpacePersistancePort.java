package pe.edu.upeu.microserviceenviroment.application.ports.output;

import pe.edu.upeu.microserviceenviroment.domain.model.AcademicSpace;

import java.util.List;
import java.util.Optional;

public interface AcademicSpacePersistancePort {
    Optional<AcademicSpace> findById(Long id);
    List<AcademicSpace> findAll();
    AcademicSpace save(AcademicSpace academicSpace);
    void deleteById(Long id);
}
