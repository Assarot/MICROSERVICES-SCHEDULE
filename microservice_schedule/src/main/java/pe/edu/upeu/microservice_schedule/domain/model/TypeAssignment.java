package pe.edu.upeu.microservice_schedule.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypeAssignment {
    private Long idSchedule;
    private String name;
    private Boolean isActive;
}
