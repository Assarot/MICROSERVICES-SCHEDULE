package pe.edu.upeu.microserviceimport.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.microserviceimport.dto.CourseResponseDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseAssignmentCourseResponseDTO {
    private Long idCourseAssignmentCourse;
    private CourseResponseDTO course;
    private CourseAssignmentResponseDTO courseAssignment;
}
