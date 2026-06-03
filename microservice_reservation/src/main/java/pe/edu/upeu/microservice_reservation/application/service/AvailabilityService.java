package pe.edu.upeu.microservice_reservation.application.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pe.edu.upeu.microservice_reservation.application.dto.response.AvailabilityResponse;
import pe.edu.upeu.microservice_reservation.infrastructure.client.AcademicSpaceClient;
import pe.edu.upeu.microservice_reservation.infrastructure.client.dto.AcademicSpaceClientDto;
import pe.edu.upeu.microservice_reservation.infrastructure.adapter.out.persistence.repository.ReservationJpaRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de disponibilidad de espacios académicos.
 * Consulta MS-ENVIRONMENT para obtener los espacios y filtra
 * los que tienen reservas aprobadas o bloqueos de horario que se solapen.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final AcademicSpaceClient academicSpaceClient;
    private final ReservationJpaRepository reservationRepository;

    @Value("${reservation.max-advance-days:365}")
    private int maxAdvanceDays;

    /**
     * Consulta disponibilidad de espacios académicos para un rango de fecha/hora.
     *
     * @param startDatetime inicio del rango
     * @param endDatetime fin del rango
     * @param minCapacity capacidad mínima requerida (nullable)
     * @param typeAcademicSpace tipo de espacio (aula, laboratorio, auditorio) (nullable)
     * @param idAcademicSpace ID de espacio específico (nullable)
     * @return lista de espacios disponibles con sus datos
     */
    public List<AvailabilityResponse> checkAvailability(
            LocalDateTime startDatetime,
            LocalDateTime endDatetime,
            Integer minCapacity,
            String typeAcademicSpace,
            Long idAcademicSpace) {

        log.info("Consultando disponibilidad: start={}, end={}, minCapacity={}, type={}, idSpace={}",
                startDatetime, endDatetime, minCapacity, typeAcademicSpace, idAcademicSpace);

        // Validar límite de 1 año
        long daysDiff = ChronoUnit.DAYS.between(LocalDateTime.now(), startDatetime);
        if (daysDiff > maxAdvanceDays) {
            throw new IllegalArgumentException(
                "No se puede consultar disponibilidad más de " + maxAdvanceDays +
                " días en el futuro. Fecha máxima permitida: " +
                LocalDateTime.now().plusDays(maxAdvanceDays));
        }

        // Obtener espacios desde MS-ENVIRONMENT
        List<AcademicSpaceClientDto> allSpaces;
        try {
            if (idAcademicSpace != null) {
                AcademicSpaceClientDto space = academicSpaceClient.getAcademicSpaceById(idAcademicSpace);
                allSpaces = List.of(space);
            } else {
                allSpaces = academicSpaceClient.getAllAcademicSpaces();
            }
        } catch (FeignException e) {
            log.error("Error al obtener espacios académicos de MS-ENVIRONMENT: {}", e.getMessage());
            throw new RuntimeException("No se pudo obtener información de espacios académicos");
        }

        return allSpaces.stream()
                // Filtrar por capacidad mínima
                .filter(space -> minCapacity == null || space.getCapacity() >= minCapacity)
                // Filtrar por tipo de espacio
                .filter(space -> typeAcademicSpace == null ||
                        (space.getTypeAcademicSpace() != null &&
                         space.getTypeAcademicSpace().getName() != null &&
                         space.getTypeAcademicSpace().getName().equalsIgnoreCase(typeAcademicSpace)))
                // Verificar si NO tiene reservas aprobadas que se solapen
                .filter(space -> {
                    boolean overlaps = reservationRepository.existsApprovedOverlap(
                            space.getIdAcademicSpace(), startDatetime, endDatetime, null);
                    if (overlaps) {
                        log.debug("Espacio {} tiene solapamiento con reserva aprobada", space.getIdAcademicSpace());
                    }
                    return !overlaps;
                })
                // Mapear a AvailabilityResponse
                .map(space -> AvailabilityResponse.builder()
                        .idAcademicSpace(space.getIdAcademicSpace())
                        .spaceName(space.getSpaceName())
                        .location(space.getLocation())
                        .capacity(space.getCapacity())
                        .typeAcademicSpace(space.getTypeAcademicSpace() != null ?
                                space.getTypeAcademicSpace().getName() : null)
                        .observation(space.getObservation())
                        .startDatetime(startDatetime)
                        .endDatetime(endDatetime)
                        .available(true)
                        .build())
                .collect(Collectors.toList());
    }
}
