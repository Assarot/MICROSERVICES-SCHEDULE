package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.response;

import lombok.*;

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
    private StateResponse state;
    private FloorResponse floor;
    private TypeAcademicSpaceResponse typeAcademicSpace;
}
