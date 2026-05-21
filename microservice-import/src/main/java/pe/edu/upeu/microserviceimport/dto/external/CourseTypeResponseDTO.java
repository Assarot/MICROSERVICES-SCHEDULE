package pe.edu.upeu.microserviceimport.dto.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseTypeResponseDTO {
    private Long idCourseType;
    private String name;
}
