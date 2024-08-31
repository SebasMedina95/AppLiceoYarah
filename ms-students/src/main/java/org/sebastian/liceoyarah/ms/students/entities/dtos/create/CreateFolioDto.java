package org.sebastian.liceoyarah.ms.students.entities.dtos.create;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateFolioDto {

    @NotEmpty(message = "El número de folio es requerido")
    @Size(min = 5, max = 40, message = "El número de folio debe ser mínimo de 5 caracteres y máximo de 40")
    private String numberFolio;

    @NotEmpty(message = "La resolución es requerida")
    @Size(min = 5, max = 50, message = "La resolución debe ser mínimo de 5 caracteres y máximo de 50")
    private String resolution;

    private String description;

}
