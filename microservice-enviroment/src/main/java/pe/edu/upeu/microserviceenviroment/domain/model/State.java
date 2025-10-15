package pe.edu.upeu.microserviceenviroment.domain.model;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class State {
    private long idState;
    private String name;
    private char isActive;
}
