package pe.edu.upeu.microservice_auth.infrastructure.adapter.input.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogoutRequestDTO {

    @JsonProperty("access_token")
    @JsonAlias({"accessToken"})
    private String accessToken;

    @JsonProperty("refresh_token")
    @JsonAlias({"refreshToken"})
    private String refreshToken;
}
