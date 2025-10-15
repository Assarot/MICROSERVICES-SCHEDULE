package pe.edu.upeu.microservice_course_management.domain.model;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {
    private Long idTeacher;
    private String name;
    private String lastName;
    private String email;
}
