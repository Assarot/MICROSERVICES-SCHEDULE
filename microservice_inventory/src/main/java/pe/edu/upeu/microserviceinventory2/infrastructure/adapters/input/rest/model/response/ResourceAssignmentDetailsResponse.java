package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.response;

import lombok.Data;

@Data
public class ResourceAssignmentDetailsResponse {
    private Long idResourceAssignment;
    private Long idAcademicSpace;

    // Resource fields
    private String resourceCode;
    private Integer resourceStock;
    private String resourcePhotoUrl;
    private String resourceStateName;
    private String resourceTypeName;

    // Academic Space fields
    private Long academicSpaceId;
    private String spaceName;
    private Integer capacity;
    private String typeAcademicSpaceName;
    private Integer floorNumber;
    private String buildingName;
}
