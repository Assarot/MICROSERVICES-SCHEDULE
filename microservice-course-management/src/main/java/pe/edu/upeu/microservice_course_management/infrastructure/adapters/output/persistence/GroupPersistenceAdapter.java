package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_course_management.application.ports.output.GroupPersistencePort;
import pe.edu.upeu.microservice_course_management.domain.model.Group;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.mapper.GroupPersistenceMapper;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.repository.GroupRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GroupPersistenceAdapter implements GroupPersistencePort {

    private final GroupRepository repository;
    private final GroupPersistenceMapper mapper;

    @Override
    public Optional<Group> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toGroup);
    }

    @Override
    public List<Group> findAll() {
        return mapper.toGroupList(repository.findAll());
    }

    @Override
    public Group save(Group group) {
        var entity = mapper.toGroupEntity(group);
        var savedEntity = repository.save(entity);
        return mapper.toGroup(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
