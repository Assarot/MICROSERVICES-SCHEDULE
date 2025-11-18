package pe.edu.upeu.microservice_user.infrastructure.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(
            @Value("${cloudinary.cloud_name}") String cloudName,
            @Value("${cloudinary.api_key}") String apiKey,
            @Value("${cloudinary.api_secret}") String apiSecret
    ) {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        this.cloudinary = new Cloudinary(config);
    }

    public String uploadImage(MultipartFile file, String folder) throws IOException {
        // Límite de tamaño: 10 MB
        long maxFileSize = 10 * 1024 * 1024; // 10 MB

        // Verificamos si el archivo excede el tamaño máximo
        if (file.getSize() > maxFileSize) {
            throw new IOException("El archivo excede el tamaño máximo permitido de 10 MB.");
        }

        Map<String, Object> params = ObjectUtils.asMap(
                "folder", folder,
                "resource_type", "image",
                "overwrite", true
        );

        // Subir archivo a Cloudinary
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), params);

        // Obtener la URL segura del archivo subido
        Object url = uploadResult.get("secure_url");
        return url != null ? url.toString() : null;
    }
}
