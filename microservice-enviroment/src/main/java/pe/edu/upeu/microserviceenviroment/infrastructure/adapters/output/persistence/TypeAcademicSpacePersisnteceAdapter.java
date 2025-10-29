package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microserviceenviroment.application.ports.output.TypeAcademicSpacePersistancePort;
import pe.edu.upeu.microserviceenviroment.domain.model.TypeAcademicSpace;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.entity.TypeAcademicSpaceEntity;
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
        try {
            if (typeAcademicSpace.getIdTypeAcademicSpace() > 0) {
                return repository.findById(typeAcademicSpace.getIdTypeAcademicSpace())
                        .map(existing ->{
                            existing.setName(typeAcademicSpace.getName());
                            existing.setIsActive(typeAcademicSpace.getIsActive());
                            TypeAcademicSpaceEntity saved = repository.save(existing);
                            return mapper.toTypeAcademicSpace(saved);
                        }).orElseGet(()-> mapper.toTypeAcademicSpace(repository.save(mapper.toTypeAcademicSpaceEntity(typeAcademicSpace))));
            }
        } catch (Exception e) {

        }
        return mapper.toTypeAcademicSpace(repository.save(mapper.toTypeAcademicSpaceEntity(typeAcademicSpace)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
