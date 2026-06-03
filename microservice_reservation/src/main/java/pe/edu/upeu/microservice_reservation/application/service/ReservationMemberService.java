package pe.edu.upeu.microservice_reservation.application.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.microservice_reservation.application.dto.request.AddMemberRequest;
import pe.edu.upeu.microservice_reservation.application.dto.response.ReservationMemberResponse;
import pe.edu.upeu.microservice_reservation.infrastructure.adapter.out.persistence.entity.ReservationEntity;
import pe.edu.upeu.microservice_reservation.infrastructure.adapter.out.persistence.entity.ReservationMemberEntity;
import pe.edu.upeu.microservice_reservation.infrastructure.adapter.out.persistence.repository.ReservationJpaRepository;
import pe.edu.upeu.microservice_reservation.infrastructure.adapter.out.persistence.repository.ReservationMemberJpaRepository;
import pe.edu.upeu.microservice_reservation.infrastructure.adapter.out.persistence.repository.ReservationStatusJpaRepository;
import pe.edu.upeu.microservice_reservation.infrastructure.client.AcademicSpaceClient;
import pe.edu.upeu.microservice_reservation.infrastructure.client.UserProfileClient;
import pe.edu.upeu.microservice_reservation.infrastructure.client.dto.AcademicSpaceClientDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de integrantes de una reserva.
 * Valida capacidad antes de agregar integrantes.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationMemberService {

    private final ReservationMemberJpaRepository memberRepository;
    private final ReservationJpaRepository reservationRepository;
    private final UserProfileClient userProfileClient;
    private final AcademicSpaceClient academicSpaceClient;

    @Transactional
    public ReservationMemberResponse addMember(Long idReservation, AddMemberRequest request,
            Long authenticatedUserProfileId) {

        ReservationEntity reservation = reservationRepository.findById(idReservation)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + idReservation));

        // Validar que el user profile del integrante existe
        try {
            userProfileClient.getUserProfileById(request.getIdUserProfile());
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("Usuario no encontrado con id_user_profile: " + request.getIdUserProfile());
        }

        // Verificar que no sea duplicado
        if (memberRepository.existsByReservation_IdReservationAndIdUserProfile(
                idReservation, request.getIdUserProfile())) {
            throw new IllegalArgumentException("El usuario ya es integrante de esta reserva.");
        }

        // Obtener capacidad del espacio
        AcademicSpaceClientDto space;
        try {
            space = academicSpaceClient.getAcademicSpaceById(reservation.getIdAcademicSpace());
        } catch (FeignException e) {
            throw new RuntimeException("Error al consultar espacio académico.");
        }

        // Contar miembros actuales + solicitante (1) + nuevo integrante (1)
        int currentMembers = memberRepository.countByReservation_IdReservation(idReservation);
        int totalAfterAdd = 1 + currentMembers + 1; // 1=solicitante + actuales + nuevo
        if (totalAfterAdd > space.getCapacity()) {
            throw new IllegalArgumentException(
                "No se puede agregar más integrantes. Capacidad del espacio: " + space.getCapacity() +
                ". Total tras agregar: " + totalAfterAdd);
        }

        ReservationMemberEntity member = ReservationMemberEntity.builder()
                .reservation(reservation)
                .idUserProfile(request.getIdUserProfile())
                .build();

        member = memberRepository.save(member);
        log.info("Integrante {} agregado a reserva {}", request.getIdUserProfile(), idReservation);

        return toResponse(member);
    }

    public List<ReservationMemberResponse> getMembersByReservation(Long idReservation) {
        reservationRepository.findById(idReservation)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + idReservation));
        return memberRepository.findByReservation_IdReservation(idReservation)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public void removeMember(Long idReservation, Long idMember) {
        ReservationMemberEntity member = memberRepository.findById(idMember)
                .orElseThrow(() -> new RuntimeException("Integrante no encontrado con id: " + idMember));

        if (!member.getReservation().getIdReservation().equals(idReservation)) {
            throw new IllegalArgumentException(
                "El integrante " + idMember + " no pertenece a la reserva " + idReservation);
        }

        memberRepository.delete(member);
        log.info("Integrante {} eliminado de reserva {}", idMember, idReservation);
    }

    private ReservationMemberResponse toResponse(ReservationMemberEntity entity) {
        return ReservationMemberResponse.builder()
                .idReservationMember(entity.getIdReservationMember())
                .idReservation(entity.getReservation().getIdReservation())
                .idUserProfile(entity.getIdUserProfile())
                .build();
    }
}
