package pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseToAssign {
    private Long idCourseAssignment;
    private Integer capacityRequired;
    private String preferredType; // optional
    private List<Long> candidateAcademicSpaceIds; // optional - if empty, caller must provide candidates
    private Long idTypeSchedule; // optional - theory (1) or practice (2)
    private Integer hoursRequired; // optional
    private Long idTeacher; // optional - for teacher collision validation
    private Long idGroup; // optional - for group collision validation
    private Integer priority; // optional - for MRV heuristic weighting
}
