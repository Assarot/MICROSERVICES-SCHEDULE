package pe.edu.upeu.microservice_reservation.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationMemberResponse {

    private Long idReservationMember;
    private Long idReservation;
    private Long idUserProfile;
}
