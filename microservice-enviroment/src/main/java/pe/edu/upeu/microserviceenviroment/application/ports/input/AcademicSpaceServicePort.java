package pe.edu.upeu.microserviceenviroment.application.ports.input;

import pe.edu.upeu.microserviceenviroment.domain.model.AcademicSpace;

import java.util.List;

public interface AcademicSpaceServicePort {
    AcademicSpace findById(Long id);
    List<AcademicSpace> findAll();
    AcademicSpace save(AcademicSpace academicSpace);
    AcademicSpace update(Long id,AcademicSpace academicSpace);
    void deleteById(Long id);
}
