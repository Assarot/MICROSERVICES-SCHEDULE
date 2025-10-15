package pe.edu.upeu.microservice_course_management.application.ports.input;

import pe.edu.upeu.microservice_course_management.domain.model.Group;

import java.util.List;

public interface GroupServicePort {
    Group findById(Long id);
    List<Group> findAll();
    Group save(Group group);
    Group update(Long id, Group group);
    void deleteById(Long id);
}
