package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.upeu.microservice_course_management.domain.model.CourseAssignmentCourse;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "course_assignment")
public class CourseAssignmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_course_assignment")
    private Long idCourseAssignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_teacher", nullable = false)
    private TeacherEntity teacher;

    @OneToMany(mappedBy = "courseAssignment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CourseAssignmentCourseEntity> courseAssignmentCourse;
}
