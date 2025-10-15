package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_course_management.application.ports.output.ProfessionalSchoolPersistencePort;
import pe.edu.upeu.microservice_course_management.domain.model.ProfessionalSchool;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.mapper.ProfessionalSchoolPersistenceMapper;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.repository.CycleRepository;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.repository.FacultyRepository;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.repository.ProfessionalSchoolRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProfessionalSchoolPersistenceAdapter implements ProfessionalSchoolPersistencePort {

    private final ProfessionalSchoolRepository repository;
    private final ProfessionalSchoolPersistenceMapper mapper;
    private final FacultyRepository facultyRepository;

    @Override
    public Optional<ProfessionalSchool> findById(long id) {
        return repository.findById(id)
                .map(mapper::toProfessionalSchool);
    }

    @Override
    public List<ProfessionalSchool> findAll() {
        return mapper.toProfessionalSchoolList(repository.findAll());
    }

    @Override
    public ProfessionalSchool save(ProfessionalSchool professionalSchool) {
        var entity = mapper.toProfessionalSchoolEntity(professionalSchool);
        var savedEntity = repository.save(entity);
        return mapper.toProfessionalSchool(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
