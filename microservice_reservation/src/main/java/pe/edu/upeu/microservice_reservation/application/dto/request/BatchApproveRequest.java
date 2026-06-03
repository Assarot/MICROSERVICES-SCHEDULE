package pe.edu.upeu.microservice_reservation.application.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchApproveRequest {

    @NotNull(message = "reservationIds es requerido")
    @NotEmpty(message = "reservationIds no puede estar vacío")
    private List<Long> reservationIds;

    private String changeReason;
}
