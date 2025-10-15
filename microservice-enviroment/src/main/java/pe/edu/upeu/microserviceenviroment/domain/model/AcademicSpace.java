package pe.edu.upeu.microserviceenviroment.domain.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcademicSpace {
    private Long idAcademicSpace;
    private String spaceName;
    private String observation;
    private String location;
    private int capacity;
    private State state;
    private Floor floor;
    private TypeAcademicSpace typeAcademicSpace;
}
