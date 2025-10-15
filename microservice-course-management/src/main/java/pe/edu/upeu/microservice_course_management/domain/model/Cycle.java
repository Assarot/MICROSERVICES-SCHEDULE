package pe.edu.upeu.microservice_course_management.domain.model;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cycle {
    private Long idCycle;
    private String name;
    private ProfessionalSchool professionalSchool;
}
