package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.respose;

import lombok.*;
import pe.edu.upeu.microservice_course_management.domain.model.Cycle;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupResponse {
    private Long idGroup;
    private String groupNumber;
    private int capacity;
    private Cycle cycle;
}
