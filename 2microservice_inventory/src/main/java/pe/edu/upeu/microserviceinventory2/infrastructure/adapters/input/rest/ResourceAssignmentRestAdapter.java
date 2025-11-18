package pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microserviceinventory2.application.ports.input.ResourceAssignmentServicePort;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.mapper.ResourceAssignmentRestMapper;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.request.ResourceAssignmentRequest;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.response.ResourceAssignmentResponse;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.input.rest.model.response.ResourceAssignmentDetailsResponse;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.external.feign.EnvironmentFeignClient;
import pe.edu.upeu.microserviceinventory2.infrastructure.adapters.output.external.feign.dto.EnvAcademicSpaceDTO;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resource-assignments")
public class ResourceAssignmentRestAdapter {

    private final ResourceAssignmentServicePort servicePort;
    private final ResourceAssignmentRestMapper restMapper;
    private final EnvironmentFeignClient environmentFeignClient;

    @PostMapping
    public ResponseEntity<ResourceAssignmentResponse> create(@Valid @RequestBody ResourceAssignmentRequest request) {
        var created = servicePort.save(restMapper.toModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(restMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public ResourceAssignmentResponse update(@PathVariable("id") Long id, @Valid @RequestBody ResourceAssignmentRequest request) {
        return restMapper.toResponse(servicePort.update(id, restMapper.toModel(request)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        servicePort.deleteById(id);
    }

    @GetMapping("/{id}")
    public ResourceAssignmentResponse getById(@PathVariable("id") Long id) {
        return restMapper.toResponse(servicePort.findById(id));
    }

    @GetMapping
    public List<ResourceAssignmentResponse> getAll() {
        return restMapper.toResponseList(servicePort.findAll());
    }

    @GetMapping("/details")
    public List<ResourceAssignmentDetailsResponse> getAllWithDetails() {
        var assignments = servicePort.findAll();
        List<ResourceAssignmentDetailsResponse> result = new ArrayList<>();
        for (var ra : assignments) {
            ResourceAssignmentDetailsResponse dto = new ResourceAssignmentDetailsResponse();
            dto.setIdResourceAssignment(ra.getIdResourceAssignment());
            dto.setIdAcademicSpace(ra.getIdAcademicSpace());

            // Resource fields
            if (ra.getResource() != null) {
                dto.setResourceCode(ra.getResource().getCode());
                dto.setResourceStock(ra.getResource().getStock());
                dto.setResourcePhotoUrl(ra.getResource().getResourcePhotoUrl());
                if (ra.getResource().getState() != null) {
                    dto.setResourceStateName(ra.getResource().getState().getName());
                }
                if (ra.getResource().getResourceType() != null) {
                    dto.setResourceTypeName(ra.getResource().getResourceType().getName());
                }
            }

            // Academic Space via Feign (fallback to null fields on error)
            try {
                EnvAcademicSpaceDTO env = environmentFeignClient.getById(ra.getIdAcademicSpace());
                if (env != null) {
                    dto.setAcademicSpaceId(env.getIdAcademicSpace());
                    dto.setSpaceName(env.getSpaceName());
                    dto.setCapacity(env.getCapacity());
                    if (env.getTypeAcademicSpace() != null) {
                        dto.setTypeAcademicSpaceName(env.getTypeAcademicSpace().getName());
                    }
                    if (env.getFloor() != null) {
                        dto.setFloorNumber(env.getFloor().getFloorNumber());
                        if (env.getFloor().getBuilding() != null) {
                            dto.setBuildingName(env.getFloor().getBuilding().getName());
                        }
                    }
                }
            } catch (Exception ex) {
                // leave academic space fields as null by design
            }

            result.add(dto);
        }
        return result;
    }

    @GetMapping("/resource/{resourceId}")
    public List<ResourceAssignmentResponse> getByResource(@PathVariable("resourceId") Long resourceId) {
        return restMapper.toResponseList(servicePort.findByResourceId(resourceId));
    }

    @GetMapping("/academic-space/{academicSpaceId}")
    public List<ResourceAssignmentResponse> getByAcademicSpace(@PathVariable("academicSpaceId") Long academicSpaceId) {
        return restMapper.toResponseList(servicePort.findByAcademicSpaceId(academicSpaceId));
    }
}
