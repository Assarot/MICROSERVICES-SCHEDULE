package pe.edu.upeu.microserviceimport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrimaryDataImportResult {
    private int statesCreated;
    private int typesCreated;
    private int buildingsCreated;
    private int floorsCreated;
    private int academicSpacesCreated;
    private String message;
}