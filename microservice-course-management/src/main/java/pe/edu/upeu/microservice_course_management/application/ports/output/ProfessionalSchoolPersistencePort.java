package pe.edu.upeu.microservice_course_management.application.ports.output;

import pe.edu.upeu.microservice_course_management.domain.model.ProfessionalSchool;

import java.util.List;
import java.util.Optional;

public interface ProfessionalSchoolPersistencePort {
    Optional<ProfessionalSchool> findById(long id);
    List<ProfessionalSchool> findAll();
    ProfessionalSchool save(ProfessionalSchool professionalSchool);
    void deleteById(Long id);

}
