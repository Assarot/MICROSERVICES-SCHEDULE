package pe.edu.upeu.microservice_course_management.application.ports.input;

import pe.edu.upeu.microservice_course_management.domain.model.ProfessionalSchool;

import java.util.List;

public interface ProfessionalSchoolServicePort {
    ProfessionalSchool findById(Long id);
    List<ProfessionalSchool> findAll();
    ProfessionalSchool save(ProfessionalSchool professionalSchool);
    ProfessionalSchool update(Long id, ProfessionalSchool professionalSchool);
    void deleteById(Long id);
}
