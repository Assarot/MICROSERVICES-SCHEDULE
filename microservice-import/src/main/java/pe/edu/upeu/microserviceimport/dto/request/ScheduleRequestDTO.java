package pe.edu.upeu.microserviceimport.dto.request;

import lombok.Data;

@Data
public class ScheduleRequestDTO {
    private Long courseId;
    private Long teacherId;
    private Long groupId;
    private Long academicSpaceId;

    private String day;
    private String startTime;
    private String endTime;
}
