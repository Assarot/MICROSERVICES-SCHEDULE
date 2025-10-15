package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.respose;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherResponse {
    private Long idTeacher;
    private String name;
    private String lastName;
    private String email;
}