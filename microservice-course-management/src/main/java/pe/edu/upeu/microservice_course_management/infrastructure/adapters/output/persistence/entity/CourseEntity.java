package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.util.List;

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
    private Duration duration;
    @Column(name = "theoretical_hours")
    private Duration theoreticalHours;
    @Column(name = "practical_hours")
    private Duration practicalHours;
    @Column(name = "total_hours")
    private Duration totalHours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_course_type", nullable = false)
    private CourseTypeEntity courseType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plan", nullable = false)
    private PlanEntity plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_group", nullable = false)
    private GroupEntity group;

    @OneToMany(mappedBy = "course", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = false)
    @JsonIgnore
    private List<CourseAssignmentCourseEntity> courseAssignmentCourse;
}
