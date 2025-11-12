package pe.edu.upeu.microservice_user.infrastructure.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserResponseDto {
    private Long id;
    private String username;
    private Boolean isActive;
    private Long userProfileId;
    private Set<RoleDto> roles;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoleDto {
        private Long id;
        private String name;
    }
}
