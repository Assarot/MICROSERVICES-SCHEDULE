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
public class AutoAssignRequest {
    private List<CourseToAssign> courses;
    private List<String> startTimes; // HH:mm:ss
    private Integer durationMinutes;
    private List<Long> weekDayIds; // preferred week days (ids)
}
