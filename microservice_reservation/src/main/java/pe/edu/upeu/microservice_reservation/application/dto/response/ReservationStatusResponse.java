package pe.edu.upeu.microservice_reservation.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationStatusResponse {

    private Long idStatus;
    private String name;
    private Boolean isActive;
}
