package pe.edu.upeu.microserviceimport.config;

import org.springframework.context.annotation.Configuration;

/**
 * Configuración global de Feign Clients
 * Define timeouts y políticas de reintentos
 */
@Configuration
public class FeignConfig {
    
    // La configuración principal está en application.yml bajo feign.client.config
    // Esta clase está disponible para future customizations
    
}
