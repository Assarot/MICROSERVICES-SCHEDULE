package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.respose;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanResponse {

    private Long idPlan;
    private String name;
}
