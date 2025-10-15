package pe.edu.upeu.microserviceenviroment.application.ports.input;

import pe.edu.upeu.microserviceenviroment.domain.model.TypeAcademicSpace;

import java.util.List;

public interface TypeAcademicSpaceServicePort {
    TypeAcademicSpace findById(Long id);
    List<TypeAcademicSpace> findAll();
    TypeAcademicSpace save(TypeAcademicSpace typeAcademicSpace);
    TypeAcademicSpace update(Long id,TypeAcademicSpace typeAcademicSpace);
    void deleteById(Long id);
}
