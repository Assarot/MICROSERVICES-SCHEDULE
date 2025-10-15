package pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pe.edu.upeu.microserviceenviroment.domain.model.TypeAcademicSpace;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.request.TypeAcademicSpaceCreateRequest;
import pe.edu.upeu.microserviceenviroment.infrastructure.adapters.input.rest.model.response.TypeAcademicSpaceResponse;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy =  ReportingPolicy.IGNORE)
public interface TypeAcademicSpaceRestMapper {
    TypeAcademicSpace toTypeAcademicSpace(TypeAcademicSpaceCreateRequest request);
    TypeAcademicSpaceResponse toTypeAcademicSpaceResponse(TypeAcademicSpace typeAcademicSpace);
    List<TypeAcademicSpaceResponse> toTypeAcademicSpaceResponsesList(List<TypeAcademicSpace> typeAcademicSpaces);
}
