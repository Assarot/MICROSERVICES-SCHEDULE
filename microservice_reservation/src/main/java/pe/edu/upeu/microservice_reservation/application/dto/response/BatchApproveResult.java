package pe.edu.upeu.microservice_reservation.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Resultado individual de una reserva dentro del batch-approve.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchApproveResult {

    private Long idReservation;

    /** APPROVED / REJECTED_EXTERNAL_CONFLICT / REJECTED_BATCH_CONFLICT / INVALID_STATUS / ERROR */
    private String result;
    private String message;
}
