package pe.edu.upeu.microserviceimport.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.microserviceimport.dto.request.CourseToAssignRequest;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoAssignRequest {
    private List<CourseToAssignRequest> courses;
    private List<String> startTimes;
    private Integer durationMinutes;
    private List<Long> weekDayIds;
}
