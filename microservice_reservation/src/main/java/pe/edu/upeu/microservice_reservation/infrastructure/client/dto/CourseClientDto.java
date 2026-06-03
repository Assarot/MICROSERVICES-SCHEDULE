package pe.edu.upeu.microservice_reservation.infrastructure.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseClientDto {

    private Long idCourse;
    private String name;
    private String code;
    private String description;
    private Duration duration;
}
