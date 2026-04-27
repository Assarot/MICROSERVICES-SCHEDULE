package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.external;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class EnvironmentClient {

    private final RestTemplate restTemplate;

    @Value("${external.enviroment-base-url}")
    private String envBaseUrl;

    public boolean existsAcademicSpace(Long id) {
        try {
            var url = String.format("%s/v1/api/academic-space/%d", envBaseUrl, id);
            restTemplate.getForEntity(url, String.class);
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }
    }
}
