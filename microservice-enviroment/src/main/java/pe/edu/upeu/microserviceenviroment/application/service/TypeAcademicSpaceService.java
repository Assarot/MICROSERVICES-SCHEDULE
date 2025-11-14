package pe.edu.upeu.microserviceenviroment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upeu.microserviceenviroment.application.ports.input.TypeAcademicSpaceServicePort;
import pe.edu.upeu.microserviceenviroment.application.ports.output.TypeAcademicSpacePersistancePort;
import pe.edu.upeu.microserviceenviroment.domain.exception.TypeAcademicSpaceNotFoundException;
import pe.edu.upeu.microserviceenviroment.domain.model.TypeAcademicSpace;

import java.util.List;
@Service
@RequiredArgsConstructor
public class TypeAcademicSpaceService implements TypeAcademicSpaceServicePort {
    private final TypeAcademicSpacePersistancePort typeAcademicSpacePersistancePort;

    @Override
    public TypeAcademicSpace findById(Long id) {
        return typeAcademicSpacePersistancePort.findById(id)
                .orElseThrow(TypeAcademicSpaceNotFoundException::new);
    }

    @Override
    public List<TypeAcademicSpace> findAll() {
        return typeAcademicSpacePersistancePort.findAll();
    }

    @Override
    public TypeAcademicSpace save(TypeAcademicSpace typeAcademicSpace) {
        return typeAcademicSpacePersistancePort.save(typeAcademicSpace);
    }

    @Override
    public TypeAcademicSpace update(Long id, TypeAcademicSpace typeAcademicSpace) {
        return typeAcademicSpacePersistancePort.findById(id)
                .map(savedTypeAcademicSpace -> {
                    savedTypeAcademicSpace.setName(typeAcademicSpace.getName());
                    savedTypeAcademicSpace.setIsActive(typeAcademicSpace.getIsActive());
                    return typeAcademicSpacePersistancePort.save(savedTypeAcademicSpace);
                }).orElseThrow(TypeAcademicSpaceNotFoundException::new);
    }

    @Override
    public void deleteById(Long id) {
        if (typeAcademicSpacePersistancePort.findById(id).isEmpty()) {
            throw new TypeAcademicSpaceNotFoundException();
        }
        typeAcademicSpacePersistancePort.deleteById(id);
    }
}
