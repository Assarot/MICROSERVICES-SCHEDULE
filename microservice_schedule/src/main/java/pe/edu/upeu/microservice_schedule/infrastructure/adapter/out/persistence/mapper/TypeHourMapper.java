package pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_schedule.domain.model.TypeHour;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.entity.TypeHourEntity;

@Component
public class TypeHourMapper {
    
    public TypeHour toDomain(TypeHourEntity entity) {
        if (entity == null) {
            return null;
        }
        return TypeHour.builder()
                .idTypeHour(entity.getIdTypeHour())
                .typeHour(entity.getTypeHour())
                .build();
    }
    
    public TypeHourEntity toEntity(TypeHour domain) {
        if (domain == null) {
            return null;
        }
        return TypeHourEntity.builder()
                .idTypeHour(domain.getIdTypeHour())
                .typeHour(domain.getTypeHour())
                .build();
    }
}
