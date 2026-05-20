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
public class AutoAssignResponse {
    private List<AssignedResult> assigned;
    private List<Long> failedCourseAssignmentIds;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssignedResult {
        private Long idCourseAssignment;
        private Long idAcademicSpace;
        private String startTime;
        private String endTime;
        private Long weekDayId;
        private Long idSchedule;
    }
}
