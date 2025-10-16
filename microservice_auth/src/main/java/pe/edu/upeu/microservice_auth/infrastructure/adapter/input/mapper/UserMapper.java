package pe.edu.upeu.microservice_auth.infrastructure.adapter.input.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_auth.domain.model.AuthUser;
import pe.edu.upeu.microservice_auth.infrastructure.adapter.input.dto.UserResponseDTO;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponseDTO toResponseDTO(AuthUser user) {
        if (user == null) {
            return null;
        }

        return UserResponseDTO.builder()
                .id(user.getIdAuthUser())
                .username(user.getUsername())
                .isActive(user.getIsActive())
                .userProfileId(user.getIdUserProfile())
                .roles(user.getRoles().stream()
                        .map(role -> UserResponseDTO.RoleDTO.builder()
                                .id(role.getIdRole())
                                .name(role.getName())
                                .build())
                        .collect(Collectors.toSet()))
                .build();
    }
}
