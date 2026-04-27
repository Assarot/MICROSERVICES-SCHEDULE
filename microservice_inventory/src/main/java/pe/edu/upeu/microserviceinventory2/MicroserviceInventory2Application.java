package pe.edu.upeu.microserviceinventory2;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.CategoryResourceEntity;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.ResourceTypeEntity;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.entity.StateEntity;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.repository.CategoryResourceRepository;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.repository.ResourceTypeRepository;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.persistence.repository.StateRepository;

@SpringBootApplication
@EnableFeignClients
@RequiredArgsConstructor
public class MicroserviceInventory2Application implements CommandLineRunner {

    private final CategoryResourceRepository categoryResourceRepository;
    private final StateRepository stateRepository;
    private final ResourceTypeRepository resourceTypeRepository;
    public static void main(String[] args) {
        SpringApplication.run(MicroserviceInventory2Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        CategoryResourceEntity category1 = new CategoryResourceEntity(null, "Inmoviliario", true);
        category1 = categoryResourceRepository.save(category1);
        StateEntity state1 = new StateEntity(null, "Disponible", true);
        state1 = stateRepository.save(state1);
        ResourceTypeEntity resourceType1 = new ResourceTypeEntity(null, "Pupitre", true, category1.getIdCategoryResource(), category1 );
        resourceType1 = resourceTypeRepository.save(resourceType1);
    }
}
