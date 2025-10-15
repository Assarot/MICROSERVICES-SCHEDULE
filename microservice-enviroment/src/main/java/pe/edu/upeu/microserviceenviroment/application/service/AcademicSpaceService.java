package pe.edu.upeu.microserviceenviroment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upeu.microserviceenviroment.application.ports.input.AcademicSpaceServicePort;
import pe.edu.upeu.microserviceenviroment.application.ports.output.AcademicSpacePersistancePort;
import pe.edu.upeu.microserviceenviroment.domain.exception.AcademicSpaceNotFoundException;
import pe.edu.upeu.microserviceenviroment.domain.model.AcademicSpace;

import java.util.List;
@Service
@RequiredArgsConstructor
public class AcademicSpaceService implements AcademicSpaceServicePort {

    private final AcademicSpacePersistancePort academicSpacePersistancePort;

    @Override
    public AcademicSpace findById(Long id) {
        return academicSpacePersistancePort.findById(id)
                .orElseThrow(AcademicSpaceNotFoundException::new);
    }

    @Override
    public List<AcademicSpace> findAll() {
        return academicSpacePersistancePort.findAll();
    }

    @Override
    public AcademicSpace save(AcademicSpace academicSpace) {
        return academicSpacePersistancePort.save(academicSpace);
    }

    @Override
    public AcademicSpace update(Long id, AcademicSpace academicSpace) {
        return academicSpacePersistancePort.findById(id)
                .map(savedAcademicSpace -> {
                    savedAcademicSpace.setSpaceName(academicSpace.getSpaceName());
                    savedAcademicSpace.setObservation(academicSpace.getObservation());
                    savedAcademicSpace.setCapacity(academicSpace.getCapacity());
                    savedAcademicSpace.setLocation(academicSpace.getLocation());
                    savedAcademicSpace.setState(academicSpace.getState());
                    savedAcademicSpace.setFloor(academicSpace.getFloor());
                    savedAcademicSpace.setTypeAcademicSpace(academicSpace.getTypeAcademicSpace());
                    return academicSpacePersistancePort.save(savedAcademicSpace);
                }).orElseThrow(AcademicSpaceNotFoundException::new);
    }

    @Override
    public void deleteById(Long id) {
        if (academicSpacePersistancePort.findById(id).isEmpty()) {
            throw new AcademicSpaceNotFoundException();
        }
        academicSpacePersistancePort.deleteById(id);
    }
}
