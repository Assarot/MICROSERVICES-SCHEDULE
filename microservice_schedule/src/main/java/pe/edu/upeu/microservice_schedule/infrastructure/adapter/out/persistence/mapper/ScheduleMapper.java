package pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_schedule.domain.model.Schedule;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.entity.ScheduleEntity;

@Component
public class ScheduleMapper {
    
    public Schedule toDomain(ScheduleEntity entity) {
        if (entity == null) {
            return null;
        }
        return Schedule.builder()
                .idSchedule(entity.getIdSchedule())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .duration(entity.getDuration())
                .idTypeSchedule(entity.getIdTypeSchedule())
                .idAcademicSpace(entity.getIdAcademicSpace())
                .idCourseAssignment(entity.getIdCourseAssignment())
                .idWeekName(entity.getIdWeekName())
                .build();
    }
    
    public ScheduleEntity toEntity(Schedule domain) {
        if (domain == null) {
            return null;
        }
        return ScheduleEntity.builder()
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
