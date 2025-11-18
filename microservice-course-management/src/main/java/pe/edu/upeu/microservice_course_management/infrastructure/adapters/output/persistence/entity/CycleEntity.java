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
@Table(name = "cycles")
public class CycleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cycle")
    private Long idCycle;
    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_professional_school")
    private ProfessionalSchoolEntity professionalSchool;

    @OneToMany(mappedBy = "cycle", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<GroupEntity> groups;
}
