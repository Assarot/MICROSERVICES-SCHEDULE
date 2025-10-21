package pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_schedule.domain.model.TypeAssignment;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.dto.TypeAssignmentRequest;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.dto.TypeAssignmentResponse;

@Component
public class TypeAssignmentDtoMapper {
    
    public TypeAssignment toDomain(TypeAssignmentRequest request) {
        return TypeAssignment.builder()
                .name(request.getName())
                .isActive(request.getIsActive())
                .build();
    }
    
    public TypeAssignmentResponse toResponse(TypeAssignment domain) {
        return TypeAssignmentResponse.builder()
                .idSchedule(domain.getIdSchedule())
                .name(domain.getName())
                .isActive(domain.getIsActive())
                .build();
    }
}
