package pe.edu.upeu.microservice_reservation.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationLogResponse {

    private Long idReservationLog;
    private Long idReservation;
    private ReservationStatusResponse previousStatus;
    private ReservationStatusResponse newStatus;
    private String changeReason;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime changedAt;

    private Long idUserProfile;
}
