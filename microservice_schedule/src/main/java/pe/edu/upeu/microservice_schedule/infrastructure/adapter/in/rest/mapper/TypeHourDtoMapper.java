package pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_schedule.domain.model.TypeHour;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.dto.TypeHourRequest;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.dto.TypeHourResponse;

@Component
public class TypeHourDtoMapper {
    
    public TypeHour toDomain(TypeHourRequest request) {
        return TypeHour.builder()
                .typeHour(request.getTypeHour())
                .build();
    }
    
    public TypeHourResponse toResponse(TypeHour domain) {
        return TypeHourResponse.builder()
                .idTypeHour(domain.getIdTypeHour())
                .typeHour(domain.getTypeHour())
                .build();
    }
}
