package pe.edu.upeu.microserviceimport.dto.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupResponseDTO {
    private Long idGroup;
    private String groupNumber;
    private int capacity;
}
