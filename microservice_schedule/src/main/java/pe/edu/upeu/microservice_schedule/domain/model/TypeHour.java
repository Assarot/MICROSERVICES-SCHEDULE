package pe.edu.upeu.microservice_schedule.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypeHour {
    private Long idTypeHour;
    private String typeHour;
}
