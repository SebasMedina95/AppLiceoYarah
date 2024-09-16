package com.sebastian.liceoyarah.ms.professors.entities.dtos.create;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProfessorDto {

    @NotEmpty(message = "El numero de documento del profesor es requerido")
    @Size(min = 6, max = 30, message = "El numero de documento del profesor debe ser mínimo de 6 caracteres y máximo de 30")
    private String documentNumber;

    @NotEmpty(message = "El tipo de profesor es requerido")
    @Size(min = 1, max = 1, message = "Un solo caracter. Tipo de Profesor - 1 Planta o 2 Catedra")
    @Pattern(regexp = "[12]", message = "El tipo de profesor debe ser '1' (Planta) o '2' (Catedra)")
    private String type;

    @NotEmpty(message = "El título profesional del profesor es requerido")
    @Size(min = 10, max = 100, message = "El título del profesor debe ser mínimo de 10 caracteres y máximo de 100")
    private String proffesionalTittle;

    @NotEmpty(message = "El título profesional del profesor es requerido")
    @Size(min = 10, max = 100, message = "El título del profesor debe ser mínimo de 10 caracteres y máximo de 100")
    private String cardTittle;

    @NotNull(message = "El director de grupo es requerido")
    private Boolean groupDirector;

    @NotNull(message = "El profesor técnico es requerido")
    private Boolean technicalProfessor;

    @NotEmpty(message = "El core de enseñanza es requerido")
    @Size(min = 1, max = 1, message = "Un solo caracter. Core de enseñanza - 1 Primaria, 2 Bachillerato, 3 Media Técnica, 4 Mixto")
    @Pattern(regexp = "[1234]", message = "El core de enseñanza debe ser '1' (Primaria), '2' (Bachillerato), '3' (Media Técnica) o '4' (Mixto)")
    private String core;

    @NotEmpty(message = "La jornada laboral de trabajo es requerida")
    @Size(min = 1, max = 1, message = "Un solo caracter. Jornada laboral 1 Completa, 2 Mañana, 3 Tarde, 4 Nocturna, 5 Combinados")
    @Pattern(regexp = "[12345]", message = "La jornada laboral debe ser '1' (Completa), '2' (Mañana), '3' (Tarde), '4' (Nocturna) o '5' (Combinados)")
    private String laborDay;

    private String description;

}
