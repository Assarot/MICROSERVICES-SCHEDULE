package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.respose;

import lombok.*;
import pe.edu.upeu.microservice_course_management.domain.model.ProfessionalSchool;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CycleResponse {
    private Long idCycle;
    private String name;
    private ProfessionalSchool professionalSchool;
}
