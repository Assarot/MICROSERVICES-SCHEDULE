package pe.edu.upeu.microservice_reservation.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusChangeRequest {

    @NotBlank(message = "changeReason es requerido")
    @Size(max = 2000, message = "changeReason no debe superar 2000 caracteres")
    private String changeReason;
}
