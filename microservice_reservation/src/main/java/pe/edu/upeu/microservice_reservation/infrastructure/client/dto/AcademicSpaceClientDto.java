package pe.edu.upeu.microservice_reservation.infrastructure.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicSpaceClientDto {

    private Long idAcademicSpace;
    private String spaceName;
    private String observation;
    private String location;
    private int capacity;
    private StateClientDto state;
    private FloorClientDto floor;
    private TypeAcademicSpaceClientDto typeAcademicSpace;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StateClientDto {
        private Long idState;
        private String name;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FloorClientDto {
        private Long idFloor;
        private String name;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TypeAcademicSpaceClientDto {
        private Long idTypeAcademicSpace;
        private String name;
        private Character isActive;
    }
}
