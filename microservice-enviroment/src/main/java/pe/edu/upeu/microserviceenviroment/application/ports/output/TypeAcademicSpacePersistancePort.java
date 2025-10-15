package pe.edu.upeu.microserviceenviroment.application.ports.output;

import pe.edu.upeu.microserviceenviroment.domain.model.TypeAcademicSpace;

import java.util.List;
import java.util.Optional;

public interface TypeAcademicSpacePersistancePort {
    Optional<TypeAcademicSpace> findById(Long id);
    List<TypeAcademicSpace> findAll();
    TypeAcademicSpace save(TypeAcademicSpace typeAcademicSpace);
    void deleteById(Long id);
}
