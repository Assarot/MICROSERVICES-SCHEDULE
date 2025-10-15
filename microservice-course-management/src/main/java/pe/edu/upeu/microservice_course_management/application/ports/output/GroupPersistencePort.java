package pe.edu.upeu.microservice_course_management.application.ports.output;

import pe.edu.upeu.microservice_course_management.domain.model.Group;

import java.util.List;
import java.util.Optional;

public interface GroupPersistencePort {

    Optional<Group> findById(Long id);
    List<Group> findAll();
    Group save(Group group);
    void deleteById(Long id);

}
