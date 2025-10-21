package pe.edu.upeu.microservice_inventory.domain.port.out;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryPort {
    String uploadImage(MultipartFile file);
    void deleteImage(String publicId);
}
