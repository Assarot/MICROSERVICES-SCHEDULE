package pe.edu.upeu.microservice_course_management.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upeu.microservice_course_management.application.ports.input.CourseServicePort;
import pe.edu.upeu.microservice_course_management.application.ports.output.CoursePersistencePort;
import pe.edu.upeu.microservice_course_management.domain.exception.CourseNotFoundException;
import pe.edu.upeu.microservice_course_management.domain.model.Course;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CourseService implements CourseServicePort {

    private final CoursePersistencePort persistencePort;

    @Override
    public Course findById(Long id) {
        return persistencePort.findById(id)
                .orElseThrow(CourseNotFoundException::new);
    }

    @Override
    public List<Course> findAll() {
        return persistencePort.findAll();
    }

    @Override
    public Course save(Course course) {
        return persistencePort.save(course);
    }

    @Override
    public Course update(Long id, Course course) {
        return persistencePort.findById(id)
                .map(saveCourse -> {
                    saveCourse.setName(course.getName());
                    saveCourse.setCode(course.getCode());
                    saveCourse.setDescription(course.getDescription());
                    saveCourse.setDuration(course.getDuration());
                    saveCourse.setCreditValue(course.getCreditValue());
                    saveCourse.setTheoreticalHours(course.getTheoreticalHours());
                    saveCourse.setPracticalHours(course.getPracticalHours());
                    saveCourse.setCourseType(course.getCourseType());
                    return persistencePort.save(saveCourse);
                }).orElseThrow(CourseNotFoundException::new);
    }

    @Override
    public void deleteById(Long id) {
        if (persistencePort.findById(id).isEmpty()) {
            throw new CourseNotFoundException();
        }

        persistencePort.deleteById(id);
    }
}
