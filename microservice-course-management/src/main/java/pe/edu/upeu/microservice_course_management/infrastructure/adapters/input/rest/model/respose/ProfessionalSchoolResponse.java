package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.respose;

import lombok.*;
import pe.edu.upeu.microservice_course_management.domain.model.Faculty;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfessionalSchoolResponse {
    private Long idProfessionalSchool;
    private String name;
    private Faculty faculty;
}
