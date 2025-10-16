package pe.edu.upeu.microservice_incident.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_incident.domain.model.Severity;
import pe.edu.upeu.microservice_incident.domain.port.SeverityRepository;
import pe.edu.upeu.microservice_incident.infrastructure.repository.jpa.SeverityJpaRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SeverityRepositoryAdapter implements SeverityRepository {
    
    private final SeverityJpaRepository jpaRepository;
    
    @Override
    public Severity save(Severity severity) {
        return jpaRepository.save(severity);
    }
    
    @Override
    public Optional<Severity> findById(Long id) {
        return jpaRepository.findById(id);
    }
    
    @Override
    public List<Severity> findAll() {
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
