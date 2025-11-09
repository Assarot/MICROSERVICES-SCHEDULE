package pe.edu.upeu.microservice_auth.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_refresh_token")
    private Long idRefreshToken;

    @Column(name = "refresh_token", nullable = false, unique = true, length = 500)
    private String refreshToken;

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_auth_user", nullable = false)
    private AuthUser authUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_auth_session")
    private AuthSession authSession;

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiryDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RefreshToken that)) return false;
        return idRefreshToken != null && idRefreshToken.equals(that.idRefreshToken);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
