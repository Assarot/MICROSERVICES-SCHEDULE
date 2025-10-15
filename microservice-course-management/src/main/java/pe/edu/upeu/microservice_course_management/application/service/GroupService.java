package pe.edu.upeu.microservice_course_management.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upeu.microservice_course_management.application.ports.input.GroupServicePort;
import pe.edu.upeu.microservice_course_management.application.ports.output.GroupPersistencePort;
import pe.edu.upeu.microservice_course_management.domain.exception.GroupNotFoundException;
import pe.edu.upeu.microservice_course_management.domain.model.Group;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService implements GroupServicePort {

    private final GroupPersistencePort persistencePort;

    @Override
    public Group findById(Long id) {
        return persistencePort.findById(id)
                .orElseThrow(GroupNotFoundException::new);
    }

    @Override
    public List<Group> findAll() {
        return persistencePort.findAll();
    }

    @Override
    public Group save(Group group) {
        return persistencePort.save(group);
    }

    @Override
    public Group update(Long id, Group group) {
        return persistencePort.findById(id)
                .map(saveGroup -> {
                    saveGroup.setGroupNumber(group.getGroupNumber());
                    saveGroup.setCapacity(group.getCapacity());
                    saveGroup.setCycle(group.getCycle());
                    return persistencePort.save(saveGroup);
                }).orElseThrow(GroupNotFoundException::new);
    }

    @Override
    public void deleteById(Long id) {
        if (persistencePort.findById(id).isEmpty()) {
            throw new GroupNotFoundException();
        }
        persistencePort.deleteById(id);
    }
}
