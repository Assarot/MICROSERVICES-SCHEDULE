package pe.edu.upeu.microservice_user.infrastructure.rest.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.microservice_user.application.dto.UserProfileRequestDto;
import pe.edu.upeu.microservice_user.application.dto.UserProfileResponseDto;
import pe.edu.upeu.microservice_user.application.mapper.UserProfileMapper;
import pe.edu.upeu.microservice_user.domain.exception.UserProfileNotFoundException;
import pe.edu.upeu.microservice_user.domain.model.UserProfile;
import pe.edu.upeu.microservice_user.domain.port.in.UserProfileServicePort;

import java.util.List;

/**
 * Controlador REST para UserProfile
 * Expone los endpoints CRUD del microservicio
 */
@RestController
@RequestMapping("/api/v1/user-profiles")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UserProfileController {
    
    private final UserProfileServicePort userProfileService;
    private final UserProfileMapper mapper;
    
    /**
     * Crear un nuevo perfil de usuario
     * POST /api/v1/user-profiles
     */
    @PostMapping
    public ResponseEntity<UserProfileResponseDto> createUserProfile(
            @Valid @RequestBody UserProfileRequestDto requestDto) {
        
        log.info("POST /api/v1/user-profiles - Creando perfil con email: {}", requestDto.getEmail());
        
        UserProfile userProfile = mapper.toDomain(requestDto);
        UserProfile createdProfile = userProfileService.createUserProfile(userProfile);
        UserProfileResponseDto responseDto = mapper.toResponseDto(createdProfile);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    
    /**
     * Obtener un perfil por ID
     * GET /api/v1/user-profiles/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponseDto> getUserProfileById(@PathVariable Long id) {
        
        log.info("GET /api/v1/user-profiles/{} - Consultando perfil", id);
        
        UserProfile userProfile = userProfileService.getUserProfileById(id)
                .orElseThrow(() -> new UserProfileNotFoundException(id));
        
        UserProfileResponseDto responseDto = mapper.toResponseDto(userProfile);
        return ResponseEntity.ok(responseDto);
    }
    
    /**
     * Obtener un perfil por email
     * GET /api/v1/user-profiles/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserProfileResponseDto> getUserProfileByEmail(@PathVariable String email) {
        
        log.info("GET /api/v1/user-profiles/email/{} - Consultando perfil por email", email);
        
        UserProfile userProfile = userProfileService.getUserProfileByEmail(email)
                .orElseThrow(() -> new UserProfileNotFoundException("No se encontró perfil con email: " + email));
        
        UserProfileResponseDto responseDto = mapper.toResponseDto(userProfile);
        return ResponseEntity.ok(responseDto);
    }
    
    /**
     * Listar todos los perfiles activos
     * GET /api/v1/user-profiles/active
     */
    @GetMapping("/active")
    public ResponseEntity<List<UserProfileResponseDto>> getAllActiveUserProfiles() {
        
        log.info("GET /api/v1/user-profiles/active - Consultando perfiles activos");
        
        List<UserProfile> userProfiles = userProfileService.getAllActiveUserProfiles();
        List<UserProfileResponseDto> responseDtos = mapper.toResponseDtoList(userProfiles);
        
        return ResponseEntity.ok(responseDtos);
    }
    
    /**
     * Listar todos los perfiles
     * GET /api/v1/user-profiles
     */
    @GetMapping
    public ResponseEntity<List<UserProfileResponseDto>> getAllUserProfiles() {
        
        log.info("GET /api/v1/user-profiles - Consultando todos los perfiles");
        
        List<UserProfile> userProfiles = userProfileService.getAllUserProfiles();
        List<UserProfileResponseDto> responseDtos = mapper.toResponseDtoList(userProfiles);
        
        return ResponseEntity.ok(responseDtos);
    }
    
    /**
     * Actualizar un perfil existente
     * PUT /api/v1/user-profiles/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserProfileResponseDto> updateUserProfile(
            @PathVariable Long id,
            @Valid @RequestBody UserProfileRequestDto requestDto) {
        
        log.info("PUT /api/v1/user-profiles/{} - Actualizando perfil", id);
        
        UserProfile userProfile = mapper.toDomain(requestDto);
        UserProfile updatedProfile = userProfileService.updateUserProfile(id, userProfile);
        UserProfileResponseDto responseDto = mapper.toResponseDto(updatedProfile);
        
        return ResponseEntity.ok(responseDto);
    }
    
    /**
     * Eliminar un perfil (eliminación física)
     * DELETE /api/v1/user-profiles/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserProfile(@PathVariable Long id) {
        
        log.info("DELETE /api/v1/user-profiles/{} - Eliminando perfil", id);
        
        userProfileService.deleteUserProfile(id);
        
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Desactivar un perfil (eliminación lógica)
     * PATCH /api/v1/user-profiles/{id}/deactivate
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUserProfile(@PathVariable Long id) {
        
        log.info("PATCH /api/v1/user-profiles/{}/deactivate - Desactivando perfil", id);
        
        userProfileService.deactivateUserProfile(id);
        
        return ResponseEntity.ok().build();
    }
    
    /**
     * Activar un perfil
     * PATCH /api/v1/user-profiles/{id}/activate
     */
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateUserProfile(@PathVariable Long id) {
        
        log.info("PATCH /api/v1/user-profiles/{}/activate - Activando perfil", id);
        
        userProfileService.activateUserProfile(id);
        
        return ResponseEntity.ok().build();
    }
}
