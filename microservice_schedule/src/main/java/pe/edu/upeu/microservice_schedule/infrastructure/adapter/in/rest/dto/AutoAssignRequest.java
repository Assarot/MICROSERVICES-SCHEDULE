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

    // Dynamic Activity Blocks Config
    private Long culturaDayId;
    private String culturaStartTime; // HH:mm:ss
    private String culturaEndTime;   // HH:mm:ss

    private Long activateDayId;
    private String activateStartTime; // HH:mm:ss
    private String activateEndTime;   // HH:mm:ss

    // Optional limitation by Building
    private Long idBuilding;
}
