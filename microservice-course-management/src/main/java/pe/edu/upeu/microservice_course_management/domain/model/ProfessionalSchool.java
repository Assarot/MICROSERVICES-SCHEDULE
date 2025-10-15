package pe.edu.upeu.microservice_course_management.domain.model;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfessionalSchool {
    private Long idProfessionalSchool;
    private String name;
    private Faculty faculty;
}
