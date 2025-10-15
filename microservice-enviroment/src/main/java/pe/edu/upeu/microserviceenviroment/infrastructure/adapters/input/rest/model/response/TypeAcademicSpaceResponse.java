package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.response;

import lombok.*;
import pe.edu.upeu.microserviceenviroment.domain.model.AcademicSpace;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TypeAcademicSpaceResponse {
    private Long idTypeAcademicSpace;
    private String name;
    private char isActive;
}
