package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.response;

import lombok.*;
import pe.edu.upeu.microserviceenviroment.domain.model.AcademicSpace;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StateResponse {
    private long idState;
    private String name;
    private char isActive;
}
