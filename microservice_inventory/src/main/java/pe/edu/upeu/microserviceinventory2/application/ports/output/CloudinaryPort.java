package pe.edu.upeu.microserviceinventory2.application.ports.output;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryPort {
    String uploadImage(MultipartFile file);
    void deleteImage(String publicId);
}
