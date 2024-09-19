package com.sebastian.liceoyarah.ms.professors.entities.dtos.update;

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
public class UpdateProfessorDto {

    @NotEmpty(message = "El tipo de profesor es requerido")
    @Size(min = 1, max = 50, message = "Un solo caracter. Tipo de Profesor - 1 Planta o 2 Catedra")
    @Pattern(regexp = "Planta|Catedra", message = "El tipo de profesor debe ser 'Planta' o 'Catedra'")
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
    @Size(min = 1, max = 50, message = "Core de enseñanza - Primaria, Bachillerato, Media Tecnica, Mixto")
    @Pattern(regexp = "Primaria|Bachillerato|Media Tecnica|Mixto", message = "El core de enseñanza debe ser 'Primaria', 'Bachillerato', 'Media Tecnica' o 'Mixto'")
    private String core;

    @NotEmpty(message = "La jornada laboral de trabajo es requerida")
    @Size(min = 1, max = 50, message = "Un solo caracter. Jornada laboral - Dia, Tarde, Noche, Mixta")
    @Pattern(regexp = "Dia|Tarde|Noche|Mixta", message = "La jornada laboral debe ser 'Dia', 'Tarde', 'Noche' o 'Mixta'")
    private String laborDay;

    private String description;

}
