package pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_schedule.domain.model.Schedule;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.dto.ScheduleRequest;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.dto.ScheduleResponse;

@Component
public class ScheduleDtoMapper {
    
    public Schedule toDomain(ScheduleRequest request) {
        return Schedule.builder()
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .duration(request.getDuration())
                .idTypeSchedule(request.getIdTypeSchedule())
                .idAcademicSpace(request.getIdAcademicSpace())
                .idCourseAssignment(request.getIdCourseAssignment())
                .idWeekName(request.getIdWeekName())
                .build();
    }
    
    public ScheduleResponse toResponse(Schedule domain) {
        return ScheduleResponse.builder()
                .idSchedule(domain.getIdSchedule())
                .startTime(domain.getStartTime())
                .endTime(domain.getEndTime())
                .duration(domain.getDuration())
                .idTypeSchedule(domain.getIdTypeSchedule())
                .idAcademicSpace(domain.getIdAcademicSpace())
                .idCourseAssignment(domain.getIdCourseAssignment())
                .idWeekName(domain.getIdWeekName())
                .build();
    }
}
