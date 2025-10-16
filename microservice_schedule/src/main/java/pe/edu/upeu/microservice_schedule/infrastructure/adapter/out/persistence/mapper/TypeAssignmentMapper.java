package pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_schedule.domain.model.TypeAssignment;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.entity.TypeAssignmentEntity;

@Component
public class TypeAssignmentMapper {
    
    public TypeAssignment toDomain(TypeAssignmentEntity entity) {
        if (entity == null) {
            return null;
        }
        return TypeAssignment.builder()
                .idSchedule(entity.getIdSchedule())
                .name(entity.getName())
                .isActive(entity.getIsActive())
                .build();
    }
    
    public TypeAssignmentEntity toEntity(TypeAssignment domain) {
        if (domain == null) {
            return null;
        }
        return TypeAssignmentEntity.builder()
                .idSchedule(domain.getIdSchedule())
                .name(domain.getName())
                .isActive(domain.getIsActive())
                .build();
    }
}
