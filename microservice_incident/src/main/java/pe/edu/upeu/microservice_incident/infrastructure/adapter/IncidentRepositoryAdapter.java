package pe.edu.upeu.microservice_incident.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_incident.domain.model.Incident;
import pe.edu.upeu.microservice_incident.domain.port.IncidentRepository;
import pe.edu.upeu.microservice_incident.infrastructure.repository.jpa.IncidentJpaRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class IncidentRepositoryAdapter implements IncidentRepository {
    
    private final IncidentJpaRepository jpaRepository;
    
    @Override
    public Incident save(Incident incident) {
        return jpaRepository.save(incident);
    }
    
    @Override
    public Optional<Incident> findById(Long id) {
        return jpaRepository.findById(id);
    }
    
    @Override
    public List<Incident> findAll() {
        return jpaRepository.findAll();
    }
    
    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
