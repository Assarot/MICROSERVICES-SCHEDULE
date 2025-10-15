package pe.edu.upeu.microservice_course_management.infrastructure.adapters.input.rest.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import pe.edu.upeu.microservice_course_management.domain.model.CourseMode;
import pe.edu.upeu.microservice_course_management.domain.model.CourseType;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseCreateRequest {
    @NotBlank(message = "Field name cannot be empty or null")
    private String name;
    @NotBlank(message = "Field code cannot be empty or null")
    private String code;
    @NotBlank(message = "Field description cannot be empty or null")
    private String description;
    @NotNull(message = "Field duration be empty or null")
    private int duration;
    @NotNull(message = "Field credit_value be empty or null")
    private int creditValue;
    @NotNull(message = "Field theoreticalHours be empty or null")
    private int theoreticalHours;
    @NotNull(message = "Field practicalHours be empty or null")
    private int practicalHours;
    @NotNull(message = "Field IdCourseType cannot be empty or null")
    private Long idCourseType;
    @NotNull(message = "Field IdCourseMode cannot be empty or null")
    private Long idCourseMode;
    @NotNull(message = "Field IdPlan cannot be null")
    private Long idPlan;
    @NotNull(message = "Field IdGroup cannot be null")
    private Long idGroup;
}
