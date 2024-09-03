package org.sebastian.liceoyarah.ms.students.entities.dtos.create;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateStudentDto {

    @NotNull(message = "El folio es requerido")
    private Long folioId;

    @NotEmpty(message = "El numero de documento del estudiante es requerido")
    @Size(min = 6, max = 30, message = "El numero de documento del estudiante debe ser mínimo de 6 caracteres y máximo de 30")
    private String documentNumber;

    @NotEmpty(message = "El año de inscripción es requerido")
    @Size(min = 4, max = 4, message = "El año de inscripción solo debe ser de 4 caracteres")
    private String yearInscription;

    @NotNull(message = "La condición especial del estudiante es requerido")
    private Boolean specialCondition;

    @NotEmpty(message = "El/Los responsables del estudiante es requerido")
    @Size(max = 1000, message = "El/Los responsables del estudiante no deben sobrepasar los 1000 caracteres")
    private List<String> personsCharge;

    private String description;

}
