package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.mapper;

import org.mapstruct.Mapper;
import pe.edu.upeu.microservice_course_management.domain.model.Plan;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.entity.PlanEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlanPersistenceMapper {
    PlanEntity toPlanEntity(Plan plan);
    Plan toPlan(PlanEntity entity);
    List<Plan> toPlanList(List<PlanEntity> entityList);
}
