package pe.edu.upeu.microservice_schedule.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {
    private Long idSchedule;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer duration;
    private Long idTypeSchedule;
    private Long idAcademicSpace;
    private Long idCourseAssignment;
    private Long idWeekName;
}
