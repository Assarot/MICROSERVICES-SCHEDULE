package pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "course_type")
public class CourseTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_course_type")
    private Long idCourseType;
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "courseType", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CourseEntity> courses;
}
