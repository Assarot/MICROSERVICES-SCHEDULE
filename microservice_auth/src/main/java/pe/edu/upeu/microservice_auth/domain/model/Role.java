package pe.edu.upeu.microservice_auth.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role")
    private Long idRole;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @ManyToMany(mappedBy = "roles")
    private Set<AuthUser> users = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role role)) return false;
        return idRole != null && idRole.equals(role.idRole);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
