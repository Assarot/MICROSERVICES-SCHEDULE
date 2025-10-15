package pe.edu.upeu.microserviceenviroment.domain.model;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TypeAcademicSpace {
    private Long idTypeAcademicSpace;
    private String name;
    private char isActive;
}
