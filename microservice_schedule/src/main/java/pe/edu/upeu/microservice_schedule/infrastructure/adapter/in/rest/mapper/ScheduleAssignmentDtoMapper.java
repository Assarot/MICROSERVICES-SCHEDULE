package pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_schedule.domain.model.ScheduleAssignment;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.dto.ScheduleAssignmentRequest;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.dto.ScheduleAssignmentResponse;

@Component
public class ScheduleAssignmentDtoMapper {
    
    public ScheduleAssignment toDomain(ScheduleAssignmentRequest request) {
        return ScheduleAssignment.builder()
                .assignmentDate(request.getAssignmentDate())
                .idSchedule(request.getIdSchedule())
                .idTypeAssignment(request.getIdTypeAssignment())
                .idUserProfile(request.getIdUserProfile())
                .isActive(request.getIsActive())
                .build();
    }
    
    public ScheduleAssignmentResponse toResponse(ScheduleAssignment domain) {
        return ScheduleAssignmentResponse.builder()
                .idScheduleAssignment(domain.getIdScheduleAssignment())
                .assignmentDate(domain.getAssignmentDate())
                .idSchedule(domain.getIdSchedule())
                .idTypeAssignment(domain.getIdTypeAssignment())
                .idUserProfile(domain.getIdUserProfile())
                .isActive(domain.getIsActive())
                .build();
    }
}
