package pe.edu.upeu.microserviceimport.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.microserviceimport.dto.external.TeacherDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseAssignmentResponseDTO {
    private Long idCourseAssignment;
    private TeacherDTO teacher;
}
