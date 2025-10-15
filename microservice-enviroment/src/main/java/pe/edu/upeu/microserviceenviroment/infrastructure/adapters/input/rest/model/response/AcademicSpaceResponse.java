package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.response;

import lombok.*;
import pe.edu.upeu.microserviceenviroment.domain.model.Floor;
import pe.edu.upeu.microserviceenviroment.domain.model.State;
import pe.edu.upeu.microserviceenviroment.domain.model.TypeAcademicSpace;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcademicSpaceResponse {
    private Long idAcademicSpace;
    private String spaceName;
    private String observation;
    private String location;
    private int capacity;
    private State state;
    private Floor floor;
    private TypeAcademicSpace typeAcademicSpace;
}
