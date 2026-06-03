package pe.edu.upeu.microserviceimport.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseToAssignRequest {
    private Long idCourseAssignment;
    private Integer capacityRequired;
    private String preferredType;
    private List<Long> candidateAcademicSpaceIds;
    private Long idTypeSchedule;
    private Integer hoursRequired;

    // Campos agregados para soportar el algoritmo MRV
    private Long idTeacher;
    private Long idGroup;
    private Integer priority;
}
