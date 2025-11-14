package pe.edu.upeu.microservice_course_management.domain.model;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseAssignmentCourse {
    private Long idCourseAssignmentCourse;
    private Course course;
    private CourseAssignment courseAssignment;
}
