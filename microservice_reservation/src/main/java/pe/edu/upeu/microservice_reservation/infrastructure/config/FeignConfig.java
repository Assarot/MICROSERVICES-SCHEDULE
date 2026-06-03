package pe.edu.upeu.microservice_reservation.infrastructure.config;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Configuración global de Feign clients.
 * Propaga el token JWT del request entrante a las llamadas salientes
 * hacia otros microservicios (MS-USER, MS-ENVIRONMENT, MS-COURSE).
 * MS-SCHEDULE no tiene seguridad, pero el interceptor es transparente.
 */
@Slf4j
@Configuration
public class FeignConfig {

    /**
     * Interceptor que copia el header Authorization de cada request
     * hacia todas las llamadas Feign salientes.
     */
    @Bean
    public RequestInterceptor bearerTokenRequestInterceptor() {
        return requestTemplate -> {
            try {
                ServletRequestAttributes attributes =
                        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    String authHeader = attributes.getRequest().getHeader("Authorization");
                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        requestTemplate.header("Authorization", authHeader);
                        log.debug("Token JWT propagado a Feign request: {}", requestTemplate.url());
                    }
                }
            } catch (Exception e) {
                log.warn("No se pudo propagar el token JWT al Feign request: {}", e.getMessage());
            }
        };
    }
}
