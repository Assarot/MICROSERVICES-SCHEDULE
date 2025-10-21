package pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_schedule.domain.model.WeekDay;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.entity.WeekDayEntity;

@Component
public class WeekDayMapper {
    
    public WeekDay toDomain(WeekDayEntity entity) {
        if (entity == null) {
            return null;
        }
        return WeekDay.builder()
                .idSchedule(entity.getIdSchedule())
                .name(entity.getName())
                .isActive(entity.getIsActive())
                .build();
    }
    
    public WeekDayEntity toEntity(WeekDay domain) {
        if (domain == null) {
            return null;
        }
        return WeekDayEntity.builder()
                .idSchedule(domain.getIdSchedule())
                .name(domain.getName())
                .isActive(domain.getIsActive())
                .build();
    }
}
