package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "courses")
public class CourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_course")
    private Long idCourse;
    @Column(name = "name")
    private String name;
    @Column(name = "code")
    private String code;
    @Column(name = "description")
    private String description;
    @Column(name = "duration")
    private int duration;
    @Column(name = "credit_value")
    private int creditValue;
    @Column(name = "theoretical_hours")
    private int theoreticalHours;
    @Column(name = "practical_hours")
    private int practicalHours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_course_type", nullable = false)
    private CourseTypeEntity courseType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_course_mode", nullable = false)
    private CourseModeEntity courseMode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plan", nullable = false)
    private PlanEntity plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_group", nullable = false)
    private GroupEntity group;
}
