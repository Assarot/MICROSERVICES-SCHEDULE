package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import pe.edu.upeu.microservice_course_management.domain.model.Cycle;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupCreateRequest {

    @NotBlank(message = "Field groupNumber cannot be empty or null")
    private String groupNumber;

    @NotNull(message = "Field capacity cannot be null")
    private int capacity;

    @NotNull(message = "Field inCycle cannot be null")
    private Long idCycle;
}
