package pe.edu.upeu.microservice_course_management.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upeu.microservice_course_management.application.ports.input.ProfessionalSchoolServicePort;
import pe.edu.upeu.microservice_course_management.application.ports.output.ProfessionalSchoolPersistencePort;
import pe.edu.upeu.microservice_course_management.domain.exception.ProfessionalSchoolNotFoudException;
import pe.edu.upeu.microservice_course_management.domain.model.ProfessionalSchool;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessionalSchoolService implements ProfessionalSchoolServicePort {

    private final ProfessionalSchoolPersistencePort persistencePort;

    @Override
    public ProfessionalSchool findById(Long id) {
        return persistencePort.findById(id)
                .orElseThrow(ProfessionalSchoolNotFoudException::new);
    }

    @Override
    public List<ProfessionalSchool> findAll() {
        return persistencePort.findAll();
    }

    @Override
    public ProfessionalSchool save(ProfessionalSchool professionalSchool) {
        return persistencePort.save(professionalSchool);
    }

    @Override
    public ProfessionalSchool update(Long id, ProfessionalSchool professionalSchool) {
        return persistencePort.findById(id)
                .map(saveProfessionalSchool -> {
                    saveProfessionalSchool.setName(professionalSchool.getName());
                    saveProfessionalSchool.setFaculty(professionalSchool.getFaculty());
                    return persistencePort.save(saveProfessionalSchool);
                }).orElseThrow(ProfessionalSchoolNotFoudException::new);
    }

    @Override
    public void deleteById(Long id) {
        if (persistencePort.findById(id).isEmpty()) {
            throw new ProfessionalSchoolNotFoudException();
        }

        persistencePort.deleteById(id);
    }
}
