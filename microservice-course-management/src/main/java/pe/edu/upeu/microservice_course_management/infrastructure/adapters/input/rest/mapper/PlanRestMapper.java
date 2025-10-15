package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.mapper;

import jakarta.persistence.MapKeyEnumerated;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pe.edu.upeu.microservice_course_management.domain.model.Plan;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request.PlanCreateRequest;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.respose.PlanResponse;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PlanRestMapper {
    Plan toPlan(PlanCreateRequest request);
    PlanResponse toPlanResponse(Plan plan);
    List<PlanResponse> toPlanResposeList(List<Plan> courses);
}
