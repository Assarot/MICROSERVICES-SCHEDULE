package pe.edu.upeu.microservice_course_management.domain.model;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    private Long idGroup;
    private String groupNumber;
    private int capacity;
    private Cycle cycle;
}
