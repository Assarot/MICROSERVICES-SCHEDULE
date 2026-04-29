package pe.edu.upeu.microserviceimport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseAssignmentCourseDTO {
    private Long idCourseAssignmentCourse;
    private Long idCourse;
    private Long idCourseAssignment;
}