package pe.edu.upeu.microservice_reservation.application.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationCreateRequest {

    @NotNull(message = "startDatetime es requerido")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDatetime;

    @NotNull(message = "endDatetime es requerido")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDatetime;

    @NotBlank(message = "reason es requerido")
    @Size(max = 150, message = "reason no debe superar 150 caracteres")
    private String reason;

    @Size(max = 5000, message = "description no debe superar 5000 caracteres")
    private String description;

    @NotNull(message = "idAcademicSpace es requerido")
    private Long idAcademicSpace;

    /** NULL si es actividad extracurricular */
    private Long idCourse;

    /** Lista opcional de IDs de integrantes (id_user_profile) */
    private List<Long> memberIds;

    /**
     * Clave de idempotencia para evitar duplicados por doble clic.
     * El cliente debe generar un UUID y enviarlo con cada request.
     */
    private String idempotencyKey;
}
