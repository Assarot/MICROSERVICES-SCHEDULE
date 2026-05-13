package pe.edu.upeu.microserviceimport.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AcademicSpaceCreateRequest {
    @NotBlank(message = "Field space_name cannot be empty or null")
    private String spaceName;
    @NotBlank(message = "Field observation cannot be empty or null")
    private String observation;
    @NotBlank(message = "Field location cannot be empty or null")
    private String location;
    @NotNull(message = "Field capacity cannot be empty or null")
    private int capacity;
    @NotNull(message = "Field id_state cannot be empty or null")
    private Long idState;
    @NotNull(message = "Field id_floor cannot be empty or null")
    private Long idFloor;
    @NotNull(message = "Field id_type_academic_space cannot be empty or null")
    private Long idTypeAcademicSpace;

    public AcademicSpaceCreateRequest() {}

    public AcademicSpaceCreateRequest(String spaceName, String observation, String location,
                                    int capacity, Long idState, Long idFloor, Long idTypeAcademicSpace) {
        this.spaceName = spaceName;
        this.observation = observation;
        this.location = location;
        this.capacity = capacity;
        this.idState = idState;
        this.idFloor = idFloor;
        this.idTypeAcademicSpace = idTypeAcademicSpace;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Long getIdState() {
        return idState;
    }

    public void setIdState(Long idState) {
        this.idState = idState;
    }

    public Long getIdFloor() {
        return idFloor;
    }

    public void setIdFloor(Long idFloor) {
        this.idFloor = idFloor;
    }

    public Long getIdTypeAcademicSpace() {
        return idTypeAcademicSpace;
    }

    public void setIdTypeAcademicSpace(Long idTypeAcademicSpace) {
        this.idTypeAcademicSpace = idTypeAcademicSpace;
    }
}