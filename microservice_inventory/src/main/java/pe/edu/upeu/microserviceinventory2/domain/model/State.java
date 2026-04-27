package pe.edu.upeu.microserviceinventory2.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class State {
    private Long idState;
    private String name;
    private Boolean isActive;
}
