package pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_schedule.domain.model.WeekDay;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.dto.WeekDayRequest;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.dto.WeekDayResponse;

@Component
public class WeekDayDtoMapper {
    
    public WeekDay toDomain(WeekDayRequest request) {
        return WeekDay.builder()
                .name(request.getName())
                .isActive(request.getIsActive())
                .build();
    }
    
    public WeekDayResponse toResponse(WeekDay domain) {
        return WeekDayResponse.builder()
                .idSchedule(domain.getIdSchedule())
                .name(domain.getName())
                .isActive(domain.getIsActive())
                .build();
    }
}
