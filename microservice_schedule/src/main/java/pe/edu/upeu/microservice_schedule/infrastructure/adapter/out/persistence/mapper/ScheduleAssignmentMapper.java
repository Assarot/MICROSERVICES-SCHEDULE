package pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_schedule.domain.model.ScheduleAssignment;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.entity.ScheduleAssignmentEntity;

@Component
public class ScheduleAssignmentMapper {
    
    public ScheduleAssignment toDomain(ScheduleAssignmentEntity entity) {
        if (entity == null) {
            return null;
        }
        return ScheduleAssignment.builder()
                .idScheduleAssignment(entity.getIdScheduleAssignment())
                .assignmentDate(entity.getAssignmentDate())
                .idSchedule(entity.getIdSchedule())
                .idTypeAssignment(entity.getIdTypeAssignment())
                .idUserProfile(entity.getIdUserProfile())
                .isActive(entity.getIsActive())
                .build();
    }
    
    public ScheduleAssignmentEntity toEntity(ScheduleAssignment domain) {
        if (domain == null) {
            return null;
        }
        return ScheduleAssignmentEntity.builder()
                .idScheduleAssignment(domain.getIdScheduleAssignment())
                .assignmentDate(domain.getAssignmentDate())
                .idSchedule(domain.getIdSchedule())
                .idTypeAssignment(domain.getIdTypeAssignment())
                .idUserProfile(domain.getIdUserProfile())
                .isActive(domain.getIsActive())
                .build();
    }
}
