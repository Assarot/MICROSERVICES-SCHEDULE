package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microserviceenviroment.application.ports.output.TypeAcademicSpacePersistancePort;
import pe.edu.upeu.microserviceenviroment.domain.model.TypeAcademicSpace;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.mapper.TypeAcademicSpacePersistenceMapper;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.repository.TypeAcademicSpaceRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TypeAcademicSpacePersisnteceAdapter implements TypeAcademicSpacePersistancePort {

    private final TypeAcademicSpaceRepository repository;
    private final TypeAcademicSpacePersistenceMapper mapper;

    @Override
    public Optional<TypeAcademicSpace> findById(Long id) {
        return repository.findById(id).map(mapper::toTypeAcademicSpace);
    }

    @Override
    public List<TypeAcademicSpace> findAll() {
        return mapper.toTypeAcademicSpaceList(repository.findAll());
    }

    @Override
    public TypeAcademicSpace save(TypeAcademicSpace typeAcademicSpace) {
        return mapper.toTypeAcademicSpace(repository.save(mapper.toTypeAcademicSpaceEntity(typeAcademicSpace)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
