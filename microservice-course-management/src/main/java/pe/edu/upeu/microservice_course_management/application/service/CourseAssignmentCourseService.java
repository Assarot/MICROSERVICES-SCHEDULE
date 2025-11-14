package pe.edu.upeu.microservice_course_management.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upeu.microservice_course_management.application.ports.input.CourseAssignmentCourseServicePort;
import pe.edu.upeu.microservice_course_management.application.ports.output.CourseAssignmentCoursePersistencePort;
import pe.edu.upeu.microservice_course_management.domain.exception.CourseAssignmentCourseNotFoundException;
import pe.edu.upeu.microservice_course_management.domain.model.CourseAssignmentCourse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseAssignmentCourseService implements CourseAssignmentCourseServicePort {

    private final CourseAssignmentCoursePersistencePort persistencePort;

    @Override
    public CourseAssignmentCourse findById(Long id) {
        return persistencePort.findById(id).orElseThrow(CourseAssignmentCourseNotFoundException::new);
    }

    @Override
    public List<CourseAssignmentCourse> findAll() {
        return persistencePort.findAll();
    }

    @Override
    public CourseAssignmentCourse save(CourseAssignmentCourse courseAssignmentCourse) {
        return persistencePort.save(courseAssignmentCourse);
    }

    @Override
    public CourseAssignmentCourse update(Long id, CourseAssignmentCourse courseAssignmentCourse) {
        return persistencePort.findById(id)
                .map(savedCourse -> {
                    savedCourse.setCourse(courseAssignmentCourse.getCourse());
                    savedCourse.setCourseAssignment(courseAssignmentCourse.getCourseAssignment());
                    return persistencePort.save(savedCourse);
                }).orElseThrow(CourseAssignmentCourseNotFoundException::new);
    }

    @Override
    public void deleteById(Long id) {
        if (persistencePort.findById(id).isEmpty()) {
            throw new CourseAssignmentCourseNotFoundException();
        }
        persistencePort.deleteById(id);
    }
}
