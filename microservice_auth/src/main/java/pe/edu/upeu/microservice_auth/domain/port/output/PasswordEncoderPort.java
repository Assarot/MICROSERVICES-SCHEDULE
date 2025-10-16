package pe.edu.upeu.microservice_auth.domain.port.output;

public interface PasswordEncoderPort {
    
    String encode(String rawPassword);
    
    boolean matches(String rawPassword, String encodedPassword);
}
