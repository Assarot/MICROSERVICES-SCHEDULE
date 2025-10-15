package pe.edu.upeu.microservice_course_management.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upeu.microservice_course_management.application.ports.input.CourseAssignmentServicePort;
import pe.edu.upeu.microservice_course_management.application.ports.output.CourseAssignmentPersistencePort;
import pe.edu.upeu.microservice_course_management.domain.exception.CourseAssignmentNotFoundException;
import pe.edu.upeu.microservice_course_management.domain.model.CourseAssignment;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseAssignmentService implements CourseAssignmentServicePort {

    private final CourseAssignmentPersistencePort persistencePort;

    @Override
    public CourseAssignment findById(Long id) {
        return persistencePort.findById(id)
                .orElseThrow(CourseAssignmentNotFoundException::new);
    }

    @Override
    public List<CourseAssignment> findAll() {
        return persistencePort.findAll();
    }

    @Override
    public CourseAssignment save(CourseAssignment courseAssignment) {
        return persistencePort.save(courseAssignment);
    }

    @Override
    public CourseAssignment update(Long id, CourseAssignment courseAssignment) {
        return persistencePort.findById(id)
                .map(saveCourseAssignment -> {
                    saveCourseAssignment.setCourse(courseAssignment.getCourse());
                    saveCourseAssignment.setTeacher(courseAssignment.getTeacher());
                    return persistencePort.save(saveCourseAssignment);
                }).orElseThrow(CourseAssignmentNotFoundException::new);
    }

    @Override
    public void deleteById(Long id) {
        if (persistencePort.findById(id).isEmpty()) {
            throw new CourseAssignmentNotFoundException();
        }
        persistencePort.deleteById(id);
    }
}
