package pe.edu.upeu.microservice_incident.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_incident.domain.model.Status;
import pe.edu.upeu.microservice_incident.domain.port.StatusRepository;
import pe.edu.upeu.microservice_incident.infrastructure.repository.jpa.StatusJpaRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StatusRepositoryAdapter implements StatusRepository {
    
    private final StatusJpaRepository jpaRepository;
    
    @Override
    public Status save(Status status) {
        return jpaRepository.save(status);
    }
    
    @Override
    public Optional<Status> findById(Long id) {
        return jpaRepository.findById(id);
    }
    
    @Override
    public List<Status> findAll() {
        return jpaRepository.findAll();
    }
    
    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }
}
