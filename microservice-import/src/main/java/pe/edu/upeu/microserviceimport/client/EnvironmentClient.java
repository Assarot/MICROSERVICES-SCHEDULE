package pe.edu.upeu.microserviceimport.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pe.edu.upeu.microserviceimport.dto.AcademicSpaceDTO;
import pe.edu.upeu.microserviceimport.dto.BuildingDTO;
import pe.edu.upeu.microserviceimport.dto.FloorDTO;
import pe.edu.upeu.microserviceimport.dto.StateDTO;
import pe.edu.upeu.microserviceimport.dto.TypeAcademicSpaceDTO;
import pe.edu.upeu.microserviceimport.dto.request.AcademicSpaceCreateRequest;
import pe.edu.upeu.microserviceimport.dto.request.BuildingCreateRequest;
import pe.edu.upeu.microserviceimport.dto.request.FloorCreateRequest;
import pe.edu.upeu.microserviceimport.dto.request.StateCreateRequest;
import pe.edu.upeu.microserviceimport.dto.request.TypeAcademicSpaceCreateRequest;

import java.util.List;

@FeignClient(name = "ms-enviroment", url = "${environment.client.url:http://localhost:8087}", contextId = "environmentClient")
public interface EnvironmentClient {

    // Estados
    @GetMapping("/v1/api/state")
    List<StateDTO> getAllStates();

    @PostMapping("/v1/api/state")
    StateDTO createState(@RequestBody StateCreateRequest stateRequest);

    // Tipos de espacios académicos
    @GetMapping("/v1/api/type-academic-space")
    List<TypeAcademicSpaceDTO> getAllTypeAcademicSpaces();

    @PostMapping("/v1/api/type-academic-space")
    TypeAcademicSpaceDTO createTypeAcademicSpace(@RequestBody TypeAcademicSpaceCreateRequest typeRequest);

    // Edificios
    @GetMapping("/v1/api/building")
    List<BuildingDTO> getAllBuildings();

    @PostMapping("/v1/api/building")
    BuildingDTO createBuilding(@RequestBody BuildingCreateRequest buildingRequest);

    // Pisos
    @GetMapping("/v1/api/floor")
    List<FloorDTO> getAllFloors();

    @PostMapping("/v1/api/floor")
    FloorDTO createFloor(@RequestBody FloorCreateRequest floorRequest);

    // Espacios académicos
    @GetMapping("/v1/api/academic-space")
    List<AcademicSpaceDTO> getAllAcademicSpaces();

    @GetMapping("/v1/api/academic-space/{id}")
    AcademicSpaceDTO getAcademicSpaceById(@PathVariable("id") Long id);

    @PostMapping("/v1/api/academic-space")
    AcademicSpaceDTO createAcademicSpace(@RequestBody AcademicSpaceCreateRequest spaceRequest);
}
