package pe.edu.upeu.microservice_auth.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "auth_session")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auth_session")
    private Long idAuthSession;

    @Column(name = "token", nullable = false, unique = true, length = 500)
    private String token;

    @Column(name = "expires_in", nullable = false)
    private Instant expiresIn;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(name = "logout_at")
    private Instant logoutAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_auth_user", nullable = false)
    private AuthUser authUser;

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiresIn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthSession that)) return false;
        return idAuthSession != null && idAuthSession.equals(that.idAuthSession);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
