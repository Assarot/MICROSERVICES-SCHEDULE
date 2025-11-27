package pe.edu.upeu.microserviceenviroment;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.entity.BuildingEntity;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.entity.FloorEntity;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.entity.StateEntity;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.entity.TypeAcademicSpaceEntity;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.repository.BuildingRepository;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.repository.FloorRepository;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.repository.StateRepository;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.output.persistence.repository.TypeAcademicSpaceRepository;

import java.util.ArrayList;

@SpringBootApplication
@RequiredArgsConstructor
public class MicroserviceEnviromentApplication implements CommandLineRunner{

    private final BuildingRepository buildingRepository;
    private final StateRepository stateRepository;
    private final FloorRepository floorRepository;
    private final TypeAcademicSpaceRepository typeAcademicSpaceRepository;

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceEnviromentApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        BuildingEntity build1 =  new BuildingEntity(null, "Pabellón A", 'A', new ArrayList<>());
        build1 = buildingRepository.save(build1);

        BuildingEntity build2 =  new BuildingEntity(null, "Pabellón B", 'A', new ArrayList<>());
        build2 = buildingRepository.save(build2);

        BuildingEntity build3 =  new BuildingEntity(null, "Pabellón C", 'A', new ArrayList<>());
        build3 = buildingRepository.save(build3);

        BuildingEntity build4 =  new BuildingEntity(null, "Pabellón D", 'A', new ArrayList<>());
        build4 = buildingRepository.save(build4);

        BuildingEntity build5 =  new BuildingEntity(null, "Pabellón E", 'A', new ArrayList<>());
        build5 = buildingRepository.save(build5);

        // Pabellón A - 4 Pisos
        FloorEntity floor1 = new FloorEntity(null, 1, 'A',build1,new ArrayList<>());
        floor1 = floorRepository.save(floor1);
        FloorEntity floor2 = new FloorEntity(null, 2, 'A',build1,new ArrayList<>());
        floor2 = floorRepository.save(floor2);
        FloorEntity floor3 = new FloorEntity(null, 3, 'A',build1,new ArrayList<>());
        floor3 = floorRepository.save(floor3);
        FloorEntity floor4 = new FloorEntity(null, 4, 'A',build1,new ArrayList<>());
        floor4 = floorRepository.save(floor4);

        // Pabellón B - 4 Pisos
        FloorEntity floor5 = new FloorEntity(null, 1, 'A',build2,new ArrayList<>());
        floor5 = floorRepository.save(floor5);
        FloorEntity floor6 = new FloorEntity(null, 2, 'A',build2,new ArrayList<>());
        floor6 = floorRepository.save(floor6);
        FloorEntity floor7 = new FloorEntity(null, 3, 'A',build2,new ArrayList<>());
        floor7 = floorRepository.save(floor7);
        FloorEntity floor8 = new FloorEntity(null, 4, 'A',build2,new ArrayList<>());
        floor8 = floorRepository.save(floor8);

        // Pabellón C - 4 Pisos
        FloorEntity floor9 = new FloorEntity(null, 1, 'A',build3,new ArrayList<>());
        floor9 = floorRepository.save(floor9);
        FloorEntity floor10 = new FloorEntity(null, 2, 'A',build3,new ArrayList<>());
        floor10 = floorRepository.save(floor10);
        FloorEntity floor11 = new FloorEntity(null, 3, 'A',build3,new ArrayList<>());
        floor11 = floorRepository.save(floor11);
        FloorEntity floor12 = new FloorEntity(null, 4, 'A',build3,new ArrayList<>());
        floor12 = floorRepository.save(floor12);

        // Pabellón D - 5 Pisos
        FloorEntity floor13 = new FloorEntity(null, 1, 'A',build4,new ArrayList<>());
        floor13 = floorRepository.save(floor13);
        FloorEntity floor14 = new FloorEntity(null, 2, 'A',build4,new ArrayList<>());
        floor14 = floorRepository.save(floor14);
        FloorEntity floor15 = new FloorEntity(null, 3, 'A',build4,new ArrayList<>());
        floor15 = floorRepository.save(floor15);
        FloorEntity floor16 = new FloorEntity(null, 4, 'A',build4,new ArrayList<>());
        floor16 = floorRepository.save(floor16);
        FloorEntity floor17 = new FloorEntity(null, 5, 'A',build4,new ArrayList<>());
        floor17 = floorRepository.save(floor17);
        FloorEntity floor18 = new FloorEntity(null, 6, 'A',build4,new ArrayList<>());
        floor18 = floorRepository.save(floor18);

        // Pabellón E - 4 Pisos
        FloorEntity floor19 = new FloorEntity(null, 1, 'A',build5,new ArrayList<>());
        floor19 = floorRepository.save(floor19);
        FloorEntity floor20 = new FloorEntity(null, 2, 'A',build5,new ArrayList<>());
        floor20 = floorRepository.save(floor20);
        FloorEntity floor21 = new FloorEntity(null, 3, 'A',build5,new ArrayList<>());
        floor21 = floorRepository.save(floor21);
        FloorEntity floor22 = new FloorEntity(null, 4, 'A',build5,new ArrayList<>());
        floor22 = floorRepository.save(floor22);

        // Estados de Ambiente
        StateEntity state1 = new StateEntity(null,"Disponible", 'A', new ArrayList<>());
        state1 = stateRepository.save(state1);
        StateEntity state2 = new StateEntity(null,"Ocupado", 'A', new ArrayList<>());
        state2 = stateRepository.save(state2);
        StateEntity state3 = new StateEntity(null,"En Mantenimiento", 'A', new ArrayList<>());
        state3 = stateRepository.save(state3);

        // Tipos de Ambiente
        TypeAcademicSpaceEntity type1 = new TypeAcademicSpaceEntity(null, "Laboratorio de Software", 'A', new ArrayList<>());
        type1 = typeAcademicSpaceRepository.save(type1);
        TypeAcademicSpaceEntity type2 = new TypeAcademicSpaceEntity(null, "Laboratorio de Redes", 'A', new ArrayList<>());
        type2 = typeAcademicSpaceRepository.save(type2);
        TypeAcademicSpaceEntity type3 = new TypeAcademicSpaceEntity(null, "Laboratorio de Cómputo", 'A', new ArrayList<>());
        type3 = typeAcademicSpaceRepository.save(type3);
    }
}
