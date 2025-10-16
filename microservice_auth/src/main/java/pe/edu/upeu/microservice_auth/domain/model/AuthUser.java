package pe.edu.upeu.microservice_auth.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "auth_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auth_user")
    private Long idAuthUser;

    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "failed_logins_attempts", nullable = false)
    @Builder.Default
    private Integer failedLoginsAttempts = 0;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "id_user_profile")
    private Long idUserProfile;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "auth_user_role",
        joinColumns = @JoinColumn(name = "id_auth_user"),
        inverseJoinColumns = @JoinColumn(name = "id_role")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "authUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<RefreshToken> refreshTokens = new HashSet<>();

    @OneToMany(mappedBy = "authUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<AuthSession> authSessions = new HashSet<>();

    public void addRole(Role role) {
        this.roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
        role.getUsers().remove(this);
    }

    public void incrementFailedAttempts() {
        this.failedLoginsAttempts++;
    }

    public void resetFailedAttempts() {
        this.failedLoginsAttempts = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthUser authUser)) return false;
        return idAuthUser != null && idAuthUser.equals(authUser.idAuthUser);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
