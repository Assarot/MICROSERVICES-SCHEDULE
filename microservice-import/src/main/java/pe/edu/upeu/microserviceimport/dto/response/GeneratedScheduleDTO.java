package pe.edu.upeu.microserviceimport.dto.response;

import lombok.Data;

@Data
public class GeneratedScheduleDTO {
    private Long courseId;
    private Long teacherId;
    private Long groupId;
    private Long academicSpaceId;

    private String day;
    private String startTime;
    private String endTime;
}
