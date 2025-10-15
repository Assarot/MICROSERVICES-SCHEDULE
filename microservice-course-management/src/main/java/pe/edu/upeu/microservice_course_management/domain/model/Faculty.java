package pe.edu.upeu.microservice_course_management.domain.model;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Faculty {
    private Long idFaculty;
    private String name;
}
